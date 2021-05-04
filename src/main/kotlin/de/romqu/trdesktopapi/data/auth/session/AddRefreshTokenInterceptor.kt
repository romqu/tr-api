package de.romqu.trdesktopapi.data.auth.session

import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component


@Component
class AddRefreshTokenInterceptor(
    @Lazy
    private val sessionRepository: SessionRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val newRequest = if (request.url.toString().contains(SESSION_ENDPOINT)) {

            val session = sessionRepository.getByUuid(
                request.header(HEADER_SESSION_ID)!!
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