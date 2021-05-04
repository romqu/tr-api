package de.romqu.trdesktopapi.data.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.benmanes.caffeine.cache.Cache
import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
class WebsocketRepository(
    @Qualifier("WEB_SOCKET_CLIENT") private val client: OkHttpClient,
    private val cache: Cache<Long, WebSocket>,
    private val objectMapper: ObjectMapper,
) {

    fun create(sessionId: Long, listener: WebSocketListener): WebSocket {
        val request = Request.Builder()
            .addHeader(HEADER_SESSION_ID, sessionId.toString())
            .url("wss://api.traderepublic.com")
            .build()

        return client.newWebSocket(request, listener)
    }

    fun save(id: Long, webSocket: WebSocket) {
        cache.put(id, webSocket)
    }

    fun delete(id: Long) {
        cache.invalidate(id)
    }

    fun subscribe(sessionId: Long, subscriptionNumber: Int, dto: Any) {
        val subscriptionRequest = objectMapper.writeValueAsString(dto)
        cache.getIfPresent(sessionId)?.send(subscriptionRequest)
    }

    fun connect(sessionId: Long, dto: ConnectOutDto) {
        val connectRequest = "connect 22 ${objectMapper.writeValueAsString(dto)}"
        cache.getIfPresent(sessionId)?.send(connectRequest)
    }
}
