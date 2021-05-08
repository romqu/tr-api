package de.romqu.trdesktopapi.rest

import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller


@Controller
class TestWebSocketController {

    @MessageMapping("/hello")
    fun greeting(message: String, session: SessionEntity): String {
        return "Hello, $message"
    }
}

