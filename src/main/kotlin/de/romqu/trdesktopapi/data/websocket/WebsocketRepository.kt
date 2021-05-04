package de.romqu.trdesktopapi.data.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.benmanes.caffeine.cache.Cache
import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import de.romqu.trdesktopapi.data.websocket.connect.ConnectOutDto
import de.romqu.trdesktopapi.data.websocket.portfolio.PortfolioAggregateHistoryLightInDto
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
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
    private val mapperTypeCache: Cache<String, WebsocketDtoType>,
    private val sessionRepository: SessionRepository,
    private val portfolioAggregateHistoryLightStream: MutableSharedFlow<PortfolioAggregateHistoryLightInDto>,
) {
    private val websocketBroadcastListener = WebsocketBroadcastListener()

    fun create(sessionId: Long): WebSocket {
        val request = Request.Builder()
            .addHeader(HEADER_SESSION_ID, sessionId.toString())
            .url("wss://api.traderepublic.com")
            .build()

        return client.newWebSocket(request, websocketBroadcastListener)
    }

    fun save(id: Long, webSocket: WebSocket) {
        cache.put(id, webSocket)
    }

    fun delete(id: Long) {
        cache.invalidate(id)
    }

    fun subscribe(sessionId: Long, subscriptionNumber: Int, outDto: WebsocketOutDto) {
        val subscriptionRequest = objectMapper.writeValueAsString(outDto)
        cache.getIfPresent(sessionId)?.send(subscriptionRequest)
    }

    fun connect(sessionId: Long, dto: ConnectOutDto) {
        val connectRequest = "connect 22 ${objectMapper.writeValueAsString(dto)}"
        cache.getIfPresent(sessionId)?.send(connectRequest)
    }

    inner class WebsocketBroadcastListener : WebSocketListener() {

        private val subscriptionNumberMatcher = Regex("^[0-9]*")
        private val bodyMatcher = Regex("^[0-9]*")

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val sessionId = webSocket.request().header(HEADER_SESSION_ID)
            val subscriptionNumber = subscriptionNumberMatcher.find(text)
            val dtoValue = bodyMatcher.find(text)?.value
            val dtoType = mapperTypeCache.getIfPresent("$sessionId$subscriptionNumber")


            // TODO: move session token refresh into task via error broadcast
            if (dtoValue != null && dtoType != null) {
                when (dtoType) {
                    WebsocketDtoType.CONNECT -> {
                    }
                    WebsocketDtoType.PORTFOLIO_HISTORY_LIGHT -> portfolioAggregateHistoryLightStream.tryEmit(
                        objectMapper.readValue(dtoValue, PortfolioAggregateHistoryLightInDto::class.java)
                    )
                }
            } else if (text.contains("AUTHENTICATION_ERROR") && text.contains("Unauthorized")) {

                runBlocking {
                    val sessionTokenInDto = sessionRepository.getRemote(webSocket.request().header(
                        HEADER_SESSION_ID)!!.toLong()
                    )
                    val currentSession = sessionRepository.getById(webSocket.request().header(
                        HEADER_SESSION_ID)!!.toLong()
                    )!!

                    sessionRepository.update(with(currentSession) {
                        SessionEntity(id,
                            uuidId,
                            deviceId,
                            sessionTokenInDto.sessionToken.token,
                            refreshToken,
                            trackingId,
                            resetProcessId,
                            keypairId)
                    })
                }

            }
        }
    }
}

enum class WebsocketDtoType {
    CONNECT, PORTFOLIO_HISTORY_LIGHT
}
