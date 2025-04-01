package com.miageia2.threefortengame.aiplayer.service

import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

class AIPlayerRegisterWebSocketClient(private val gameService: GameService) {

    private lateinit var stompSession: StompSession

    fun connect() {
        val webSocketClient = StandardWebSocketClient()
        val stompClient = WebSocketStompClient(webSocketClient)
        stompClient.messageConverter = MappingJackson2MessageConverter()

        val future: CompletableFuture<StompSession> = stompClient.connectAsync(
            "ws://localhost:8082/ws-game", object : StompSessionHandlerAdapter() {
                override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                    println("✅ IA connectée au WebSocket !")
                    stompSession = session

                    // 🔹 S'abonner au topic spécifique au jeu
                    stompSession.subscribe("/topic/players/register", object : StompFrameHandler {
                        override fun getPayloadType(headers: StompHeaders): Type {
                            return RegisterGameDTO::class.java
                        }

                        override fun handleFrame(headers: StompHeaders, payload: Any?) {
                            println("📩 Notification reçue : $payload")
                            val registerGameDTO = payload as? RegisterGameDTO ?: return
                            println("🔄 Réception d'un nouvel utilisateur: $registerGameDTO")

                            // 🔹 Récupération du jeu et connexion de l'IA
                            val gamePart: GamePartDTO = gameService.getGame(registerGameDTO.gamePartId)
                            val aiPlayer = AIPlayerWebSocketClient(gamePart.id)
                            aiPlayer.connect()
                        }
                    })

                    println("🤖 IA souscrite au topic /topic/players/register")
                }

                override fun handleTransportError(session: StompSession, exception: Throwable) {
                    println("❌ Erreur WebSocket : ${exception.message}")
                }
            }
        )

        // Gestion des erreurs de connexion
        future.whenComplete { session, error ->
            if (error != null) {
                println("❌ Erreur de connexion WebSocket : ${error.message}")
            }
            AIPlayerRegisterWebSocketClient(gameService)
        }
    }
}
