package de.romqu.trdesktopapi.rest.shared.session

import de.romqu.trdesktopapi.domain.GetSessionByAuthHeaderTask
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.errorhandling.InvalidSessionExceptionHandler.InvalidSessionException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component

@Component
class HeaderWebSocketSessionMethodArgumentResolver(
    private val getSessionByAuthHeaderTask: GetSessionByAuthHeaderTask,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == SessionEntity::class.java

    override fun resolveArgument(parameter: MethodParameter, message: Message<*>): SessionEntity =
        getSessionByAuthHeaderTask.execute(
            StompHeaderAccessor.wrap(message).getFirstNativeHeader(HttpHeaders.AUTHORIZATION)
        ).doOn(success = { it }, failure = { throw InvalidSessionException })

}
