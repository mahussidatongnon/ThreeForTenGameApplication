package com.miageia2.threefortengame.core.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.TaskScheduler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    private lateinit var messageBrokerTaskScheduler: TaskScheduler

    @Autowired
    fun setMessageBrokerTaskScheduler(@Lazy taskScheduler: TaskScheduler) {
        this.messageBrokerTaskScheduler = taskScheduler
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic").setTaskScheduler(messageBrokerTaskScheduler) // Ajout du scheduler
        registry.setApplicationDestinationPrefixes("/app")
    }


    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws-game").setAllowedOriginPatterns("*") // WebSocket natif
        registry.addEndpoint("/ws-game").setAllowedOriginPatterns("*").withSockJS() // SockJS
    }


}
