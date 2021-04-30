package de.romqu.trdesktopapi.data.shared.di

import com.fasterxml.jackson.databind.ObjectMapper
import de.romqu.trdesktopapi.data.auth.account.AccountApi
import de.romqu.trdesktopapi.data.auth.account.resetdevice.ResetDeviceApi
import de.romqu.trdesktopapi.data.auth.login.LoginApi
import de.romqu.trdesktopapi.data.shared.interceptor.AddAdditionalRequestHeadersInterceptor
import de.romqu.trdesktopapi.data.shared.signrequest.SignRequestInterceptor
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

const val API_BASE_URL = "https://api.traderepublic.com/api/"

@Component
class ApiModule {

    @Bean
    fun okHttpClient(
        addAdditionalRequestHeadersInterceptor: AddAdditionalRequestHeadersInterceptor,
        signRequestInterceptor: SignRequestInterceptor
    ): OkHttpClient {

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val hostname = "localhost" /*127.0.0.1*/
        val port = 8081
        val proxy = Proxy(
            Proxy.Type.HTTP,
            InetSocketAddress(hostname, port)
        )

        return OkHttpClient.Builder().apply {
            proxy(proxy)
            sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }.build()
            addInterceptor(signRequestInterceptor)
            addInterceptor(addAdditionalRequestHeadersInterceptor)
        }.build()
    }

    @Bean
    fun retrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()


    @Bean
    fun accountApi(retrofit: Retrofit): AccountApi = retrofit.create()

    @Bean
    fun loginApi(retrofit: Retrofit): LoginApi = retrofit.create()

    @Bean
    fun resetDeviceApi(retrofit: Retrofit): ResetDeviceApi = retrofit.create()
}