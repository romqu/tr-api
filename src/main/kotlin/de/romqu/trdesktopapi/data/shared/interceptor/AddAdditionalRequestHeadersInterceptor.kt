package de.romqu.trdesktopapi.data.shared.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.stereotype.Component


@Component
class AddAdditionalRequestHeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request().newBuilder().apply {
            addHeader("Accept-Language", "en")
            addHeader("X-Zeta-Device", "Xiaomi Mi A3")
            addHeader("Accept-Encoding", "gzip, deflate")
            addHeader("User-Agent", "TradeRepublic/Android 30/App Version 1.1.5486")
            addHeader("X-Zeta-Platform", "android")
            addHeader("X-Zeta-Platform-Version", "30")
            addHeader("X-Zeta-App-Version", "1.1.5486")

        }.build()

        return chain.proceed(newRequest)
    }
}