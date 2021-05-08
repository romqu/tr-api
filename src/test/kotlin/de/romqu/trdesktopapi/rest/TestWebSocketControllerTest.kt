package de.romqu.trdesktopapi.rest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.TimeUnit


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TestWebSocketControllerTest {

    private lateinit var webSocketStompClient: WebSocketStompClient

    @LocalServerPort
    private val port: Int? = null

    @BeforeEach
    fun setup() {
        webSocketStompClient = WebSocketStompClient(StandardWebSocketClient())
        webSocketStompClient.messageConverter = StringMessageConverter()
    }

    @Test
    fun connect() {


        val stompHeaders = StompHeaders()
        stompHeaders.add("sessionToken", "a4e0c94a-35b7-4c5e-aea1-7ec73c08405e")
        stompHeaders.session = "session21432143243243243"
        val handshakeHeaders = WebSocketHttpHeaders()
        handshakeHeaders.add("sessionToken", "a4e0c94a-35b7-4c5e-aea1-7ec73c08405e")


        val sessionF = webSocketStompClient.connect(
            String.format("ws://localhost:%d/tr-api-websocket", port),
            handshakeHeaders,
            stompHeaders,
            object : StompSessionHandlerAdapter() {},

            )


        val session = sessionF.get(100, TimeUnit.SECONDS)

        session.send(stompHeaders.apply { destination = "/app/hello" }, "tom")

        Thread.sleep(10000)
    }

}