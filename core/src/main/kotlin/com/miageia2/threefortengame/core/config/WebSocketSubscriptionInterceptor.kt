package com.miageia2.threefortengame.core.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class SubscriptionInterceptor : ChannelInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(SubscriptionInterceptor::class.java)

//    override fun postReceive(message: Message<*>, channel: MessageChannel): Message<*>? {
//        val accessor = StompHeaderAccessor.wrap(message)
//
//        if (StompCommand.SUBSCRIBE == accessor.command) {
//            val sessionId = accessor.sessionId
//            val destination = accessor.destination
//            logger.info("📡 Nouvelle souscription détectée -> sessionId: $sessionId, topic: $destination")
//        }
//
//        logger.info("📡 Nouvelle message détectée -> command: ${accessor.command}")
//        println("message: $message")
//        return message
//    }

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = StompHeaderAccessor.wrap(message)

        logger.info("📡 Nouvelle message détectée -> command: ${accessor.command}")
//        println("pre_message: $message")
        val sessionId = accessor.sessionId
        val destination = accessor.destination
        logger.info(" sessionId: $sessionId, topic: $destination")

        return message
    }
}
