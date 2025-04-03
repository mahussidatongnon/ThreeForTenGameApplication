package com.miageia2.threefortengame.aiplayer.service

import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.messaging.simp.stomp.StompSession.Subscription
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type
import java.util.concurrent.CompletableFuture

class AIPlayerRegisterWebSocketClient(private val gameService: GameService) {

    private lateinit var stompSession: StompSession
    private lateinit var subscription: Subscription

    fun connect() {
        val webSocketClient = StandardWebSocketClient()
        val stompClient = WebSocketStompClient(webSocketClient)
//        stompClient.messageConverter = MappingJackson2MessageConverter()
        stompClient.messageConverter = StringMessageConverter()


        val future: CompletableFuture<StompSession> = stompClient.connectAsync(
            "ws://localhost:8082/ws-game", object : StompSessionHandlerAdapter() {
                override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
//                    println("✅ IA connectée au WebSocket !")
                    stompSession = session

                    subscription = stompSession.subscribe("/topic/players-register", object : StompFrameHandler {
                        override fun getPayloadType(headers: StompHeaders): Type {
                            return String::class.java
                        }

                        override fun handleFrame(headers: StompHeaders, payload: Any?) {
                            println("📩 With topic Notification reçue : $payload")
//                            val registerGameDTO = payload as? RegisterGameDTO ?: return
//                            println("🔄 Réception d'un nouvel utilisateur: $registerGameDTO")

                            // 🔹 Récupération du jeu et connexion de l'IA
//                            val gamePart: GamePartDTO = gameService.getGame(registerGameDTO.gamePartId)
//                            val aiPlayer = AIPlayerWebSocketClient(gamePart.id)
//                            aiPlayer.connect()
                        }
                    })

//                    stompSession.send("/topic/players-register", "Hello, world!")

//                    println("sessionID: ${stompSession.sessionId}")
//                    println("ID: ${subscription.subscriptionId}")
//                    println("receiptId: ${subscription.receiptId}")
//                    println("🤖 IA souscrite au topic /players/register")
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
            println("sessionID: ${session.sessionId}")
//            println("error: ${error}")
//            println("session: ${session}")
        }
    }
}
