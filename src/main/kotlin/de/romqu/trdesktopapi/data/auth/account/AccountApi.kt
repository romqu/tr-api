package de.romqu.trdesktopapi.data.auth.account


import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {
    @POST("/api/v1/auth/account")
    suspend fun authenticate(
        @Body dto: AuthenticateAccountOutDto,
        @Header(HEADER_SESSION_ID) sessionId: String,
    ): Response<Unit>
}