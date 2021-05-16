package de.romqu.trdesktopapi.data.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.benmanes.caffeine.cache.Cache
import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import de.romqu.trdesktopapi.data.websocket.connect.ConnectOutDto
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.*

const val URL_WEBSOCKET = "wss://api.traderepublic.com"

@Repository
class WebSocketRepository(
    @Qualifier("WEB_SOCKET_CLIENT") private val client: OkHttpClient,
    private val cache: Cache<String, WebSocket>,
    private val subscriptionNumberCache: Cache<String, Int>,
    private val subscriptionDtoTypeCache: Cache<String, WebSocketDtoType>,
    private val objectMapper: ObjectMapper,
    private val authErrorStream: MutableSharedFlow<AuthErrorInDto>,
    private val webSocketMessageStream: MutableSharedFlow<WebSocketMessageInDto>,
) {
    private val webSocketBroadcastListener = WebSocketBroadcastListener()

    class WebSocketMessageInDto(
        val sessionUuid: UUID,
        val message: String,
        val dtoType: WebSocketDtoType,
    )

    fun hasConnection(sessionId: UUID) = cache.getIfPresent(sessionId.toString()) != null

    fun create(sessionId: UUID): WebSocket {
        val request = Request.Builder()
            .addHeader(HEADER_SESSION_ID, sessionId.toString())
            .url(URL_WEBSOCKET)
            .build()

        return client.newWebSocket(request, webSocketBroadcastListener)
    }

    fun save(id: UUID, webSocket: WebSocket) {
        cache.put(id.toString(), webSocket)
    }

    fun delete(id: UUID) {
        cache.invalidate(id.toString())
    }

    fun getSubscriptionNumber(sessionId: String): Int? =
        subscriptionNumberCache.getIfPresent(sessionId)


    fun updateSubscriptionNumber(sessionId: String, number: Int) {
        subscriptionNumberCache.put(sessionId, number)
    }

    fun getSubscriptionDtoType(sessionId: String): Int? =
        subscriptionNumberCache.getIfPresent(sessionId)


    fun saveSubscriptionDtoType(
        sessionId: String,
        subscriptionNumber: Int,
        dtoType: WebSocketDtoType,
    ) {
        subscriptionDtoTypeCache.put("$sessionId$subscriptionNumber", dtoType)
    }


    fun subscribe(sessionId: String, outDto: WebSocketOutDto) {
        val subscriptionNumber = getSubscriptionNumber(sessionId)!!.plus(1)

        saveSubscriptionDtoType(sessionId, subscriptionNumber, outDto.dtoType)
        cache.getIfPresent(sessionId)?.send(objectMapper.writeValueAsString(outDto))

        updateSubscriptionNumber(sessionId, subscriptionNumber)
    }

    fun connect(sessionId: UUID, dto: ConnectOutDto, webSocket: WebSocket) {
        saveSubscriptionDtoType(sessionId.toString(), 0, dto.dtoType)
        webSocket.send("connect 22 ${objectMapper.writeValueAsString(dto)}")
    }


    inner class WebSocketBroadcastListener : WebSocketListener() {

        private val subscriptionNumberMatcher = Regex("^[0-9]*")

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {

            val sessionUuid = webSocket.request().header(HEADER_SESSION_ID)
            val subscriptionNumber = subscriptionNumberMatcher.find(text)
            val dtoType = subscriptionDtoTypeCache.getIfPresent("$sessionUuid$subscriptionNumber")

            if (sessionUuid != null && dtoType != null) {
                webSocketMessageStream.tryEmit(
                    WebSocketMessageInDto(
                        UUID.fromString(sessionUuid), text, dtoType
                    ),
                )
            } else if (text.contains("AUTHENTICATION_ERROR") && text.contains("Unauthorized")) {
                authErrorStream.tryEmit(AuthErrorInDto(UUID.fromString(sessionUuid)))

            }
        }
    }
}

enum class WebSocketDtoType {
    CONNECT, PORTFOLIO_HISTORY_LIGHT
}
