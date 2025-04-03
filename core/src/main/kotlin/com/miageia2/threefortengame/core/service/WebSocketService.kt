package com.miageia2.threefortengame.core.service

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WebSocketService(private val messagingTemplate: SimpMessagingTemplate) {

    val jsonMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule()) // Permet de gérer les dates Java 8
    }

    fun sendMessage(topic: String, message: Any) {
        val destination = "/topic$topic" // 📡 Destination STOMP
        val value = jsonMapper.writeValueAsString(message)
        messagingTemplate.convertAndSend(destination, value)
    }
}


