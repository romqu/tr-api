package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceInDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginInTrDto
import de.romqu.trdesktopapi.data.auth.login.LoginOutTrDto
import de.romqu.trdesktopapi.data.auth.login.LoginRepository
import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.login.LoginInDto
import de.romqu.trdesktopapi.shared.*
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
) {

    suspend fun execute(dto: LoginInDto, authHeader: String?): Result<Error, SessionEntity> =
        maybeCreateSession(authHeader)
            .login(dto.phoneNumber.toLong(), dto.pinNumber.toInt())
            .doOn(
                success = { updateSessionWithTokens(it.dto, it.session) },
                failure = { maybeResetDevice(it, dto) }
            )

    private fun maybeCreateSession(
        authHeader: String?,
    ): Result<Error, SessionEntity> =
        getSessionByAuthHeaderTask.execute(authHeader)
            .doIfFailureElseContinue { Result.Success(createSession()) }
            .mapError { Error.InvalidSession }


    private fun createSession(): SessionEntity {
        val keypair = createKeypairTask.execute()
        val keypairEntity = KeypairEntity(0, keypair.private.encoded, keypair.public.encoded)
        val savedKeypairEntity = keypairRepository.save(keypairEntity)
        val sessionEntity = SessionEntity(
            0,
            UUID.randomUUID(),
            UUID.randomUUID(),
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
        }.mapError { Error.UserIsLoggedIn(session) }

    }

    class LoginOut(
        val session: SessionEntity,
        val dto: LoginInTrDto,
    )

    private fun updateSessionWithTokens(
        trDto: LoginInTrDto,
        session: SessionEntity,
    ): Result<Error, SessionEntity> {
        val updatedSession = with(session) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                trDto.sessionToken.token,
                trDto.refreshToken.token,
                trDto.trackingId.toString(),
                null,
                keypairId,
            )
        }

        return Result.Success(sessionRepository.update(updatedSession))
    }

    private suspend fun maybeResetDevice(
        error: Error,
        dto: LoginInDto,
    ) = when (error) {
        is Error.UserIsLoggedIn -> {
            requestDeviceReset(dto.phoneNumber.toLong(), dto.pinNumber.toInt(), error.session)
                .updateSessionWithResetProcessId(error.session)
        }
        else -> Result.Failure(Error.AccountDoesNotExist)
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
        object InvalidSession : Error()
        class UserIsLoggedIn(val session: SessionEntity) : Error()
        object AccountDoesNotExist : Error()
        object CouldNotResetDevice : Error()

    }
}