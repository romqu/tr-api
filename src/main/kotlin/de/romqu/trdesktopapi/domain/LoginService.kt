package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceInDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginInDto
import de.romqu.trdesktopapi.data.auth.login.LoginOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginRepository
import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.shared.Result
import de.romqu.trdesktopapi.shared.map
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginService(
    private val sessionRepository: SessionRepository,
    private val loginRepository: LoginRepository,
    private val resetDeviceRepository: ResetDeviceRepository,
    private val createKeypairTask: CreateKeypairTask,
    private val keypairRepository: KeypairRepository,
) {

    suspend fun execute(
        phoneNumber: Long,
        pinNumber: Int,
        session: SessionEntity?,
    ): Result<ApiCallError, SessionEntity> =
        maybeCreateSession(session).run {
            login(phoneNumber, pinNumber, this).doOn(
                success = { loginInDto ->
                    updateSessionWithTokens(loginInDto, this)
                },
                failure = {
                    requestDeviceReset(phoneNumber, pinNumber, this)
                        .updateSessionWithResetProcessId(this)
                }
            )
        }

    private fun maybeCreateSession(session: SessionEntity?) =
        (session ?: createSession())

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

    private suspend fun login(
        phoneNumber: Long,
        pinNumber: Int,
        session: SessionEntity,
    ): Result<ApiCallError, LoginInDto> = loginRepository.login(
        LoginOutDto(
            "+49$phoneNumber", pinNumber
        ), session.uuidId
    )

    private fun updateSessionWithTokens(
        dto: LoginInDto,
        session: SessionEntity,
    ): Result<ApiCallError, SessionEntity> {
        val updatedSession = with(session) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                dto.sessionToken.token,
                dto.refreshToken.token,
                dto.trackingId.toString(),
                null,
                keypairId,
            )
        }

        return Result.Success(sessionRepository.update(updatedSession))
    }

    private suspend fun requestDeviceReset(
        phoneNumber: Long,
        pinNumber: Int,
        session: SessionEntity,
    ): Result<ApiCallError, RequestResetDeviceInDto> =
        resetDeviceRepository.requestResetDevice(RequestResetDeviceOutDto(
            "+49$phoneNumber", pinNumber
        ), session.uuidId)

    private fun Result<ApiCallError, RequestResetDeviceInDto>.updateSessionWithResetProcessId(
        session: SessionEntity,
    ): Result<ApiCallError, SessionEntity> = map { dto ->

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
}