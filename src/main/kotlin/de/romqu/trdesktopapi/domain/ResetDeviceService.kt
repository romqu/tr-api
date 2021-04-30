package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceOutDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceRepository
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.data.shared.extension.asX962
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.shared.Result
import org.springframework.stereotype.Service

@Service
class ResetDeviceService(
    private val sessionRepository: SessionRepository,
    private val keypairRepository: KeypairRepository,
    private val resetDeviceRepository: ResetDeviceRepository,
) {

    suspend fun execute(code: Int, session: SessionEntity): Result<ApiCallError, Unit> =
        getDeviceKey(session)
            .resetDevice(code, session)
            .resetProcessIdFromSession(session)

    private fun getDeviceKey(session: SessionEntity): String =
        keypairRepository.getById(session.keypairId)!!.publicKey.asX962()

    private suspend fun String.resetDevice(
        code: Int,
        session: SessionEntity,
    ): Result<ApiCallError, Unit> = resetDeviceRepository.resetDevice(
        ResetDeviceOutDto(code, this),
        session.id,
        session.resetProcessId!!.toString()
    )

    private fun Result<ApiCallError, Unit>.resetProcessIdFromSession(
        session: SessionEntity,
    ): Result<ApiCallError, Unit> {

        val updatedSession = with(session) {
            SessionEntity(
                id,
                uuidId,
                deviceId,
                token,
                refreshToken,
                trackingId,
                null,
                keypairId
            )
        }

        return doOn({
            Result.Success(sessionRepository.update(updatedSession))
        }, {
            Result.Success(sessionRepository.update(updatedSession))
        })
    }
}