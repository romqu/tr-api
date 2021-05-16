package de.romqu.trdesktopapi.domain.login

import de.mkammerer.argon2.Argon2Advanced
import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceInDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginInTrDto
import de.romqu.trdesktopapi.data.auth.login.LoginOutTrDto
import de.romqu.trdesktopapi.data.auth.login.LoginRepository
import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.domain.CreateKeypairTask
import de.romqu.trdesktopapi.domain.GetSessionByAuthHeaderTask
import de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.login.LoginInDto
import de.romqu.trdesktopapi.shared.*
import de.romqu.trdesktopapi.shared.Result.Failure
import de.romqu.trdesktopapi.shared.Result.Success
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginService(
    private val sessionRepository: SessionRepository,
    private val loginRepository: LoginRepository,
    private val resetDeviceRepository: ResetDeviceRepository,
    private val createKeypairTask: CreateKeypairTask,
    private val keypairRepository: KeypairRepository,
    private val getSessionByAuthHeaderTask: GetSessionByAuthHeaderTask,
    private val argon2: Argon2Advanced,
) {

    suspend fun execute(dto: LoginInDto, authHeader: String?): Result<Error, SessionEntity> =
        maybeCreateSession(authHeader, dto.phoneNumber)
            .login(dto.phoneNumber.toLong(), dto.pinNumber.toInt())
            .updateSessionWithTokens()
            .doIfFailureElseContinue(
                { it is Error.UserIsLoggedIn },
                { maybeResetDevice(it, dto) }
            )


    private fun maybeCreateSession(
        authHeader: String?,
        phoneNumber: String,
    ): Result<Error, SessionEntity> =
        getSessionByAuthHeaderTask.execute(authHeader)
            .mapError { Error.SessionByHeaderNotFound }
            .doIfFailureElseContinue {
                getPhoneNumberHash(phoneNumber)
                    .getSessionByPhoneNumberHash()
            }
            .doIfFailureElseContinue(
                { it is Error.SessionByHeaderPhoneNumberHashNotFound },
                {
                    Success(createSession((
                            it as Error.SessionByHeaderPhoneNumberHashNotFound).phoneNumberHash
                    ))
                })

    private fun getPhoneNumberHash(phoneNumber: String): Result<Error, String> =
        try {
            val hash = argon2.hash(
                1,
                1024,
                1,
                phoneNumber.toCharArray(),
                Charsets.UTF_8,
                "S43iZLEo".toByteArray()
            )
            Success(hash)
        } catch (ex: Exception) {
            Failure(Error.CouldNotHashPhoneNumber)
        }

    private fun Result<Error, String>.getSessionByPhoneNumberHash():
            Result<Error, SessionEntity> = flatMap { hash ->
        val session = sessionRepository.getByPhoneNumberHash(hash)

        if (session != null) Success(session)
        else Failure(Error.SessionByHeaderPhoneNumberHashNotFound(hash))
    }


    private fun createSession(phoneNumberHash: String): SessionEntity {
        val keypair = createKeypairTask.execute()
        val keypairEntity = KeypairEntity(0, keypair.private.encoded, keypair.public.encoded)
        val savedKeypairEntity = keypairRepository.save(keypairEntity)
        val sessionEntity = SessionEntity(
            0,
            UUID.randomUUID(),
            UUID.randomUUID(),
            phoneNumberHash,
            "",
            "",
            "",
            null,
            savedKeypairEntity.id,
        )
        return sessionRepository.save(sessionEntity)
    }

    private suspend fun Result<Error, SessionEntity>.login(
        phoneNumber: Long,
        pinNumber: Int,
    ): Result<Error, LoginOut> = flatMap { session ->
        loginRepository.login(
            LoginOutTrDto(
                "+49$phoneNumber", pinNumber
            ), session.uuidId
        ).map { dto ->
            LoginOut(session, dto)
        }.mapError { apiError ->
            when (apiError) {
                is ApiCallError.BadRequest -> Error.AccountDoesNotExist
                ApiCallError.Unauthorized -> Error.UserIsLoggedIn(session)
                else -> Error.AccountDoesNotExist
            }
        }

    }

    class LoginOut(
        val session: SessionEntity,
        val dto: LoginInTrDto,
    )

    private fun Result<Error, LoginOut>.updateSessionWithTokens(
    ): Result<Error, SessionEntity> = map { loginOut ->
        val updatedSession = with(loginOut.session) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                phoneNumberHash,
                loginOut.dto.sessionToken.token,
                loginOut.dto.refreshToken.token,
                loginOut.dto.trackingId.toString(),
                null,
                keypairId,
            )
        }

        return Success(sessionRepository.update(updatedSession))
    }

    private suspend fun maybeResetDevice(
        error: Error,
        dto: LoginInDto,
    ) = when (error) {
        is Error.UserIsLoggedIn -> {
            requestDeviceReset(dto.phoneNumber.toLong(), dto.pinNumber.toInt(), error.session)
                .updateSessionWithResetProcessId(error.session)
        }
        else -> Failure(Error.AccountDoesNotExist)
    }

    private suspend fun requestDeviceReset(
        phoneNumber: Long,
        pinNumber: Int,
        session: SessionEntity,
    ): Result<Error, RequestResetDeviceInDto> =
        resetDeviceRepository.requestResetDevice(RequestResetDeviceOutDto(
            "+49$phoneNumber", pinNumber
        ), session.uuidId)
            .mapError { Error.CouldNotResetDevice }

    private fun Result<Error, RequestResetDeviceInDto>.updateSessionWithResetProcessId(
        session: SessionEntity,
    ): Result<Error, SessionEntity> = map { dto ->

        val updatedSession = with(session) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                phoneNumberHash,
                token,
                refreshToken,
                trackingId,
                dto.processId,
                keypairId,
            )
        }

        sessionRepository.update(updatedSession)
    }

    sealed class Error {
        object SessionByHeaderNotFound : Error()
        class SessionByHeaderPhoneNumberHashNotFound(
            val phoneNumberHash: String,
        ) : Error()

        object CouldNotHashPhoneNumber : Error()
        object InvalidSession : Error()
        class UserIsLoggedIn(val session: SessionEntity) : Error()
        object AccountDoesNotExist : Error()
        object CouldNotResetDevice : Error()
    }
}