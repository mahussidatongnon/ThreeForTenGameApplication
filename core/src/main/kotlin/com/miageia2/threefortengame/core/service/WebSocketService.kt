package com.miageia2.threefortengame.core.service

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WebSocketService(private val messagingTemplate: SimpMessagingTemplate) {

    fun sendMessage(topic: String, message: Any) {
        val destination = "/topic$topic" // 📡 Destination STOMP
        messagingTemplate.convertAndSend(destination, message)
//        println("📩 Message envoyé à $destination: $message")
    }
}

//package com.miageia2.threefortengame.core.service
//
//import org.springframework.messaging.simp.SimpMessagingTemplate
//import org.springframework.stereotype.Service
//
//import jakarta.annotation.PostConstruct
//import org.springframework.http.HttpHeaders
//import org.springframework.messaging.converter.MappingJackson2MessageConverter
//import org.springframework.messaging.simp.stomp.*
//import org.springframework.web.socket.client.standard.StandardWebSocketClient
//import org.springframework.web.socket.messaging.WebSocketStompClient
//import java.util.concurrent.CompletableFuture
//
//@Service
//class WebSocketService {
//
//    private lateinit var stompSession: StompSession
//    @PostConstruct
//    fun connect() {
//        // ✅ Ajoute les identifiants dans les headers
//        val headers = HttpHeaders()
//        headers["login"] = "admin"
//        headers["passcode"] = "admin"
//
//
//        val webSocketClient = StandardWebSocketClient()
//        // ✅ Injecte les headers dans le WebSocketClient
//        webSocketClient.setUserProperties(mapOf("org.apache.tomcat.websocket.WS_AUTHENTICATION" to headers))
//
//        val stompClient = WebSocketStompClient(webSocketClient)
//        stompClient.messageConverter = MappingJackson2MessageConverter()
//
//        println("Try connect")
//        val future: CompletableFuture<StompSession> = stompClient.connectAsync(
//            "ws://localhost:15674/ws", object : StompSessionHandlerAdapter() {
//                override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
//                    println("✅ IA connectée au WebSocket !")
//                    stompSession = session
//
////                    stompSession.send("/app/players/register", "Hello, world!")
//                }
//
//                override fun handleTransportError(session: StompSession, exception: Throwable) {
//                    println("❌ Erreur WebSocket : ${exception.message}")
//                }
//            }
//        )
//
//        // Gestion des erreurs de connexion
//        future.whenComplete { session, error ->
//            if (error != null) {
//                println("❌ Erreur de connexion WebSocket : ${error.message}, ${error.cause}")
//            }
//            println("sessionID: ${session.sessionId}")
////            println("error: ${error}")
////            println("session: ${session}")
//        }
//    }
//
//    fun disconnect() {
//        stompSession.disconnect()
//    }
//
//    fun sendMessage(topic: String, message: Any) {
//        stompSession.send(topic, message)
//    }
//}
//

