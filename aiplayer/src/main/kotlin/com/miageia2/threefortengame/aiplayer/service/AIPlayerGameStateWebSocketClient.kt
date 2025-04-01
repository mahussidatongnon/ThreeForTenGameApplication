package com.miageia2.threefortengame.aiplayer.service

import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

class AIPlayerWebSocketClient(private val gamePartId: String) : StompSessionHandlerAdapter() {

    private lateinit var stompSession: StompSession

    fun connect() {
        val webSocketClient = StandardWebSocketClient()
        val stompClient = WebSocketStompClient(webSocketClient)
        stompClient.messageConverter = MappingJackson2MessageConverter()

        val future: CompletableFuture<StompSession> = stompClient.connectAsync(
            "ws://localhost:8082/ws-game", this
        )
        stompSession = future.get() // Bloque jusqu'à la connexion
        println("✅ IA connectée au WebSocket !")

        // S'abonner au topic spécifique au jeu
        stompSession.subscribe("/topic/games/$gamePartId/state", this)
        println("🤖 IA souscrite au topic /topic/games/$gamePartId/state")
    }

    override fun handleFrame(headers: StompHeaders, payload: Any?) {
        if (payload is String) {
            println("🔄 Mise à jour du jeu reçue pour IA: $payload")
            makeMove() // L'IA joue un coup après avoir reçu une mise à jour
        }
    }

    fun makeMove() {
        val move = GameMove("AI_Player", "coup_random")
//        stompSession.send("/app/play/$gamePartId", move)
        println("🤖 IA a joué un coup : $move")
    }

    override fun getPayloadType(headers: StompHeaders): Type {
        return String::class.java // Attend un JSON sous forme de String
    }
}

data class GameMove(val playerId: String, val move: String)
