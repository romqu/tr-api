package de.romqu.trdesktopapi.data.auth.account.resetdevice


import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceInDto
import de.romqu.trdesktopapi.data.auth.account.resetdevice.request.RequestResetDeviceOutDto
import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

const val PATH_PROCESS_ID = "processId"

interface ResetDeviceApi {
    @POST("/api/v1/auth/account/reset/device")
    suspend fun requestReset(
        @Body dto: RequestResetDeviceOutDto,
        @Header(HEADER_SESSION_ID) sessionId: String,
    ): Response<RequestResetDeviceInDto>

    @POST("/api/v1/auth/account/reset/device/{$PATH_PROCESS_ID}/key")
    suspend fun reset(
        @Body dto: ResetDeviceOutDto,
        @Path(PATH_PROCESS_ID) processId: String,
        @Header(HEADER_SESSION_ID) sessionId: String,
    ): Response<Unit>
}