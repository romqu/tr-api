package de.romqu.trdesktopapi.data.auth.login

import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body dto: LoginOutDto,
        @Header(HEADER_SESSION_ID) sessionId: String,
    ): Response<LoginInDto>
}