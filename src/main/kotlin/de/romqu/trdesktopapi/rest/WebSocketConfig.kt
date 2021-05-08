package de.romqu.trdesktopapi.rest

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.server.HandshakeInterceptor
import java.util.*


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    @Autowired
    lateinit var sessionRepository: SessionRepository

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {

        argumentResolvers.add(HeaderSessionResolver(sessionRepository))

        super.addArgumentResolvers(argumentResolvers)
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/tr-api-websocket")
            /*.setHandshakeHandler(object : HandshakeHandler {
                override fun doHandshake(
                    request: ServerHttpRequest,
                    response: ServerHttpResponse,
                    wsHandler: WebSocketHandler,
                    attributes: MutableMap<String, Any>,
                ): Boolean {
                    request
                    return true
                }

            })*/
            .addInterceptors(object : HandshakeInterceptor {
                override fun beforeHandshake(
                    request: ServerHttpRequest,
                    response: ServerHttpResponse,
                    wsHandler: WebSocketHandler,
                    attributes: MutableMap<String, Any>,
                ): Boolean {
                    return if (request is ServletServerHttpRequest) {
                        val sessionToken = request.servletRequest.getHeader("sessionToken")
                        val doesSessionExist = sessionRepository.doesExist(UUID.fromString(sessionToken))

                        if (doesSessionExist) true else {
                            response.setStatusCode(HttpStatus.UNAUTHORIZED)
                            false
                        }
                    } else false
                }

                override fun afterHandshake(
                    request: ServerHttpRequest,
                    response: ServerHttpResponse,
                    wsHandler: WebSocketHandler,
                    exception: Exception?,
                ) {
                }

            })
    }
}

@Component
class HeaderSessionResolver(
    private val sessionRepository: SessionRepository,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == SessionEntity::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, message: Message<*>): Any? {

        val sessionTokenUuid = StompHeaderAccessor.wrap(message).getNativeHeader("sessionToken")?.get(0)

        return sessionRepository.getByUuid(UUID.fromString(sessionTokenUuid)) ?: throw Exception()
    }

}


