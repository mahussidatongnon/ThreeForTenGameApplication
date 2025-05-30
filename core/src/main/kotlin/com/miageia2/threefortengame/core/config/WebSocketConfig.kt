package com.miageia2.threefortengame.core.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.TaskScheduler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig @Autowired constructor(
    @Lazy private val subscriptionInterceptor: SubscriptionInterceptor
) : WebSocketMessageBrokerConfigurer {

    @Autowired
    @Lazy
    lateinit var messageBrokerTaskScheduler: TaskScheduler

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
//        registry.enableStompBrokerRelay("/topic", "/queue")
//            .setRelayHost("localhost")
//            .setRelayPort(61613)
//            .setSystemLogin("myuser")
//            .setClientLogin("myuser2")
//            .setSystemPasscode("mypassword")
//            .setClientPasscode("mypassword")
//            .setTaskScheduler(messageBrokerTaskScheduler) // 🔥 Correction ici
        registry.enableSimpleBroker("/topic", "/queue")
            .setTaskScheduler(messageBrokerTaskScheduler)
            .setHeartbeatValue(longArrayOf(10000, 15000)) // Heartbeat toutes les 10s
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws-game").setAllowedOriginPatterns("*")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(subscriptionInterceptor)
    }

    override fun configureClientOutboundChannel(registration: ChannelRegistration) {
        registration.interceptors(subscriptionInterceptor)
    }

    @Bean
    fun messageConverter(): MappingJackson2MessageConverter {
        return MappingJackson2MessageConverter() // Retourne un convertisseur JSON
    }
}
