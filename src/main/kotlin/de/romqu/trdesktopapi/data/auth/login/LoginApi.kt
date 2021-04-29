package de.romqu.trdesktopapi.data.auth.login

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/v1/auth/login")
    suspend fun login(@Body dto: LoginOutDto): Response<LoginInDto>
}