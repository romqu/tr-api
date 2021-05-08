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
class WebsocketRepository(
    @Qualifier("WEB_SOCKET_CLIENT") private val client: OkHttpClient,
    private val cache: Cache<String, WebSocket>,
    private val dtoTypeCache: Cache<String, WebsocketDtoType>,
    private val lastSubscriptionNumberCache: Cache<String, Int>,
    private val objectMapper: ObjectMapper,
    private val authErrorStream: MutableSharedFlow<AuthErrorInDto>,
    private val websocketMessageStream: MutableSharedFlow<WebsocketMessageInDto>,
) {
    private val websocketBroadcastListener = WebsocketBroadcastListener()

    class WebsocketMessageInDto(
        val sessionUuid: UUID,
        val message: String,
        val dtoType: WebsocketDtoType,
    )

    fun hasConnection(sessionId: UUID) = cache.getIfPresent(sessionId.toString()) != null

    fun create(sessionId: UUID): WebSocket {
        val request = Request.Builder()
            .addHeader(HEADER_SESSION_ID, sessionId.toString())
            .url(URL_WEBSOCKET)
            .build()

        return client.newWebSocket(request, websocketBroadcastListener)
    }

    fun save(id: UUID, webSocket: WebSocket) {
        cache.put(id.toString(), webSocket)
    }

    fun delete(id: UUID) {
        cache.invalidate(id.toString())
    }

    fun subscribe(sessionId: String, outDto: WebSocketOutDto) {
        val subscriptionNumber = lastSubscriptionNumberCache.get(sessionId) { 0 }.plus(1)
        val subscriptionRequest = objectMapper.writeValueAsString(outDto)

        dtoTypeCache.put("$sessionId$subscriptionNumber", outDto.dtoType)
        cache.getIfPresent(sessionId)?.send(subscriptionRequest)

        lastSubscriptionNumberCache.put(sessionId, subscriptionNumber)
    }

    fun connect(sessionId: UUID, dto: ConnectOutDto, webSocket: WebSocket) {
        dtoTypeCache.put("${sessionId}0", dto.dtoType)
        val connectRequest = "connect 22 ${objectMapper.writeValueAsString(dto)}"
        webSocket.send(connectRequest)
    }


    inner class WebsocketBroadcastListener : WebSocketListener() {

        private val subscriptionNumberMatcher = Regex("^[0-9]*")

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {

            val sessionUuid = webSocket.request().header(HEADER_SESSION_ID)
            val subscriptionNumber = subscriptionNumberMatcher.find(text)
            val dtoType = dtoTypeCache.getIfPresent("$sessionUuid$subscriptionNumber")

            if (sessionUuid != null && dtoType != null) {
                websocketMessageStream.tryEmit(
                    WebsocketMessageInDto(
                        UUID.fromString(sessionUuid), text, dtoType
                    ),
                )
            } else if (text.contains("AUTHENTICATION_ERROR") && text.contains("Unauthorized")) {
                authErrorStream.tryEmit(AuthErrorInDto(UUID.fromString(sessionUuid)))

            }
        }
    }
}

enum class WebsocketDtoType {
    CONNECT, PORTFOLIO_HISTORY_LIGHT
}
