package de.romqu.trdesktopapi.rest.shared.config

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import java.util.*

@Component
class HeaderWeboscketSessionMethodArgumentResolver(
    private val sessionRepository: SessionRepository,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == SessionEntity::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, message: Message<*>): SessionEntity {

        val sessionTokenUuid = StompHeaderAccessor.wrap(message).getNativeHeader(HttpHeaders.AUTHORIZATION)
            ?: throw Exception()

        sessionRepository.getByUuid(UUID.fromString(sessionTokenUuid)) ?: throw Exception()

        val authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION)
            ?: throw  Exception()

        val authMatcher = AUTH_BEARER_PATTERN.matcher(authHeader)

        val token = authMatcher.group(1)

        val sessionUuid = try {
            UUID.fromString(token)
        } catch (ex: Exception) {
            throw Exception()
        }

        return sessionRepository.getByUuid(sessionUuid) ?: throw Exception()
    }
}
