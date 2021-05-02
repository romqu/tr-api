package de.romqu.trdesktopapi.data.auth.session

import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

const val SIGN_ALGORITHM = "SHA512withECDSA"
const val HEADER_SESSION_ID = "session-id"

@Component
class AddRefreshTokenInterceptor(
    @Lazy
    private val sessionRepository: SessionRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val newRequest = if (request.url.toString().contains(SESSION_ENDPOINT)) {

            val session = sessionRepository.getById(
                request.header(HEADER_SESSION_ID)!!.toLong()
            )

            if (session?.refreshToken != null) {
                request.newBuilder().addHeader(
                    "Authorization",
                    "Bearer " + session.refreshToken
                ).build()

            } else {
                request
            }
        } else request


        return chain.proceed(newRequest)
    }
}