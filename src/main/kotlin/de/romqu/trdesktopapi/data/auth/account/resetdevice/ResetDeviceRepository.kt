package de.romqu.trdesktopapi.data.auth.account.resetdevice

import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceInDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceOutDto
import de.romqu.trdesktopapi.data.shared.ApiCall
import de.romqu.trdesktopapi.data.shared.ApiCallDelegate
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.shared.Result
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ResetDeviceRepository(
    private val api: ResetDeviceApi,
    private val apiCallDelegate: ApiCallDelegate,
) : ApiCall by apiCallDelegate {

    suspend fun requestResetDevice(
        dto: RequestResetDeviceOutDto,
        sessionId: UUID,
    ): Result<ApiCallError, RequestResetDeviceInDto> =
        makeApiCallWith { api.requestReset(dto, sessionId.toString()) }


    suspend fun resetDevice(
        dto: ResetDeviceOutDto,
        sessionId: UUID,
        processId: String,
    ): Result<ApiCallError, Unit> =
        makeApiCallWith { api.reset(dto, processId, sessionId.toString()) }
}