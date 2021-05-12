package de.romqu.trdesktopapi.rest.shared.config

import de.romqu.trdesktopapi.rest.shared.session.HeaderWebSocketSessionMethodArgumentResolver
import de.romqu.trdesktopapi.rest.shared.session.WebSocketAuthHandshakeInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    @Autowired
    lateinit var webSocketAuthHandshakeInterceptor: WebSocketAuthHandshakeInterceptor

    @Autowired
    lateinit var headerWebSocketSessionMethodArgumentResolver: HeaderWebSocketSessionMethodArgumentResolver

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(headerWebSocketSessionMethodArgumentResolver)
        super.addArgumentResolvers(argumentResolvers)
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/tr-api-websocket")
            .addInterceptors(webSocketAuthHandshakeInterceptor)
    }
}


