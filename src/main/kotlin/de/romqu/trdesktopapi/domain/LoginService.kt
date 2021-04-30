package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceInDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginInDto
import de.romqu.trdesktopapi.data.auth.login.LoginOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginRepository
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.shared.Result
import de.romqu.trdesktopapi.shared.map
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val sessionRepository: SessionRepository,
    private val loginRepository: LoginRepository,
    private val resetDeviceRepository: ResetDeviceRepository,
) {

    suspend fun execute(phoneNumber: Long, pinNumber: Int, session: SessionEntity): Result<ApiCallError, Unit> =
        login(phoneNumber, pinNumber, session)
            .doOn({ dto ->
                updateSessionWithTokens(dto, session)
            }, {
                requestDeviceReset(phoneNumber, pinNumber, session)
                    .updateSessionWithResetProcessId(session)
            })


    private suspend fun login(
        phoneNumber: Long,
        pinNumber: Int,
        session: SessionEntity,
    ): Result<ApiCallError, LoginInDto> = loginRepository.login(
        LoginOutDto(
            phoneNumber, pinNumber
        ), session.id
    )

    private fun updateSessionWithTokens(
        dto: LoginInDto,
        session: SessionEntity,
    ): Result<ApiCallError, Unit> {
        val updatedSession = with(session) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                dto.sessionToken.token,
                dto.refreshToken.token,
                dto.trackingId.toString(),
                resetProcessId,
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
        ), session.id)

    private fun Result<ApiCallError, RequestResetDeviceInDto>.updateSessionWithResetProcessId(
        session: SessionEntity,
    ): Result<ApiCallError, Unit> = map { dto ->

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