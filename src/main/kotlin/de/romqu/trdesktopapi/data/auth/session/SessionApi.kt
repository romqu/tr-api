package de.romqu.trdesktopapi.data.auth.session

import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import retrofit2.http.GET
import retrofit2.http.Header

const val SESSION_ENDPOINT = "/api/v1/auth/session"

interface SessionApi {
    @GET(SESSION_ENDPOINT)
    suspend fun getSessionToken(@Header(HEADER_SESSION_ID) sessionId: String): SessionTokenInDto
}