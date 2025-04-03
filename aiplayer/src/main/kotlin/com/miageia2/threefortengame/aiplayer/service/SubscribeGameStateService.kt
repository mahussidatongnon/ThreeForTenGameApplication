package com.miageia2.threefortengame.aiplayer.service

import com.miageia2.threefortengame.aiplayer.utils.SubscriptionsPlayersManager
import com.miageia2.threefortengame.common.dto.core.GameStateDTO
import org.springframework.messaging.simp.stomp.*
import org.springframework.stereotype.Service
import java.lang.reflect.Type

@Service
class SubscribeGameStateService(private val webSocketService: WebSocketService, private val gameService: GameService) {


    fun handle(gamePartId: String) {
        println("🤖 IA souscrite au topic /topic/games.$gamePartId.state")
        val topic = "/topic/games.$gamePartId.state"
        webSocketService.stompSession.subscribe(topic, object : StompFrameHandler {
            override fun getPayloadType(headers: StompHeaders): Type {
                return String::class.java
            }

            override fun handleFrame(headers: StompHeaders, payload: Any?) {
                println("data : $payload")
                println("type : ${payload!!::class.java}")
                val gameStateDTO: GameStateDTO
                try {
                    gameStateDTO = webSocketService.jsonMapperObject.readValue(
                        payload as String, GameStateDTO::class.java
                    )
                    println("Conversion done")
                } catch (e: Exception) {
                    println("Erreur de conversion JSON : ${e.message}")
                    e.printStackTrace()
                    return // Arrête l'exécution si une erreur survient
                }
                println("Conversion done")
                if (gameStateDTO.id != null && gameStateDTO.currentPlayerId != null) {
                    println("Valide gameState")
                    if (SubscriptionsPlayersManager.containsGameID(gameStateDTO.gamePartId!!)) {
                        println("Valide SubscriptionsPlayersManager")
                        val subscriptionPlayerInfo = SubscriptionsPlayersManager.getItem(
                            gameStateDTO.gamePartId!!,
                            gameStateDTO.currentPlayerId!!
                        )
                        if (subscriptionPlayerInfo != null) {
                            println("Valide subscriptionPlayerInfo")
                            gameService.play(gameStateDTO)
                        } else
                            println("subscriptionPlayerInfo is null")
                    } else {
                        println("SubscriptionsPlayersManager doesn't contains gameID ${gameStateDTO.gamePartId}")
                    }
                } else {
                    println("Id or currentPlayer not found")
                }
            }
        })
    }
}