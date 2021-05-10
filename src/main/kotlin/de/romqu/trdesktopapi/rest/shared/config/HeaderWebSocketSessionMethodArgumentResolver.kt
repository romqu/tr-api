package de.romqu.trdesktopapi.rest.shared.config

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.SessionUtil
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component

@Component
class HeaderWebSocketSessionMethodArgumentResolver(
    private val sessionRepository: SessionRepository,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == SessionEntity::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, message: Message<*>): SessionEntity {

        val authHeader = StompHeaderAccessor.wrap(message).getFirstNativeHeader(HttpHeaders.AUTHORIZATION)
            ?: throw Exception()

        return SessionUtil.getSessionFromAuthHeader(authHeader, sessionRepository)
    }
}
