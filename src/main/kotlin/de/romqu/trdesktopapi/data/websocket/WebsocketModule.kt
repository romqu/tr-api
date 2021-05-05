package de.romqu.trdesktopapi.data.websocket

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import de.romqu.trdesktopapi.data.shared.di.WEB_SOCKET_CLIENT
import de.romqu.trdesktopapi.data.websocket.portfolio.PortfolioAggregateHistoryLightInDto
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Component
class WebsocketModule {

    @Bean
    fun getDtoTypeCache(): Cache<String, WebsocketDtoType> = Caffeine.newBuilder().build()

    @Bean
    fun portfolioLightHistoryBroadcastStream(): MutableSharedFlow<PortfolioAggregateHistoryLightInDto> =
        MutableSharedFlow()

    @Bean
    fun authErrorInDtoBroadcastStream(): MutableSharedFlow<AuthErrorInDto> = MutableSharedFlow()

    @Bean
    fun websocketMessageStream(): MutableSharedFlow<WebsocketRepository.WebsocketMessageInDto> = MutableSharedFlow()

    @Bean
    fun cache(): Cache<String, WebSocket> =
        Caffeine.newBuilder().maximumSize(100).build()

    @Bean(WEB_SOCKET_CLIENT)
    fun okHttpClientWebsocket(
    ): OkHttpClient {

        val sslContext = SSLContext.getInstance("SSL")
        val trustAllCerts = trustAllCerts()
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val hostname = "localhost" /*127.0.0.1*/
        val port = 8081
        val proxy = Proxy(
            Proxy.Type.HTTP,
            InetSocketAddress(hostname, port)
        )

        return OkHttpClient.Builder().apply {
            sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            hostnameVerifier { _, _ -> true }.build()
            proxy(proxy)
            readTimeout(0, TimeUnit.MILLISECONDS)
            connectTimeout(0, TimeUnit.MILLISECONDS)
            pingInterval(5, TimeUnit.SECONDS)
        }.build()
    }

    private fun trustAllCerts() = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
    })
}