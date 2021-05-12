package de.romqu.trdesktopapi.rest.shared.session

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.util.*
import java.util.regex.Pattern

val AUTH_BEARER_PATTERN: Pattern =
    Pattern.compile("^Bearer *([^ ]+) *$", Pattern.CASE_INSENSITIVE)

@Component
class WebSocketAuthHandshakeInterceptor(
    private val sessionRepository: SessionRepository,
) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val isAuthorized = if (request is ServletServerHttpRequest) {
            val authHeader = request.servletRequest.getHeader(HttpHeaders.AUTHORIZATION)
                ?: return false

            val authMatcher = AUTH_BEARER_PATTERN.matcher(authHeader)

            if (authMatcher.matches()) {
                val token = authMatcher.group(1)
                sessionRepository.doesExist(UUID.fromString(token))
            } else false

        } else false

        return if (isAuthorized) true else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            false
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
    }
}