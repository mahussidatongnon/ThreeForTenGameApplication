package com.miageia2.threefortengame.aiplayer.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Service
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.CompletableFuture

@Service
class WebSocketService {

    private lateinit var _stompSession: StompSession
    val jsonMapperObject: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule()) // Permet de gérer les dates Java 8
    }
    val stompSession: StompSession
        get() = _stompSession

    @Value("\${core.url}")
    private lateinit var CoreUrl: String

    @PostConstruct
    fun connect() {
        val webSocketClient = StandardWebSocketClient()
        val stompClient = WebSocketStompClient(webSocketClient)
        stompClient.messageConverter = StringMessageConverter()

        val future: CompletableFuture<StompSession> = stompClient.connectAsync(
            "ws://$CoreUrl/ws-game", object : StompSessionHandlerAdapter() {
                override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                    println("✅ IA connectée au WebSocket !")
                    _stompSession = session

                    // Activer les heartbeats

                }

                override fun handleTransportError(session: StompSession, exception: Throwable) {
                    println("❌ Erreur WebSocket : ${exception.message}")
                    reconnect()
                }
            }
        )

        // Gestion des erreurs de connexion
        future.whenComplete { session, error ->
            if (error != null) {
                println("❌ Erreur de connexion WebSocket : ${error.message}")
                reconnect()
            }
            println("sessionID: ${session.sessionId}")
        }
    }

    private fun reconnect() {
        println("♻️ Tentative de reconnexion WebSocket...")
        Thread.sleep(5000) // Attente avant la reconnexion
        connect()
    }
}