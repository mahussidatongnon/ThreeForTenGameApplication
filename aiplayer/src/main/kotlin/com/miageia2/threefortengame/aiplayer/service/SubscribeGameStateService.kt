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
                val gameStateDTO: GameStateDTO = webSocketService.jsonMapperObject.readValue(payload as String,
                    GameStateDTO::class.java)
                if (gameStateDTO.id != null && gameStateDTO.currentPlayerId != null)
                    if(SubscriptionsPlayersManager.containsGameID(gameStateDTO.gamePartId!!)){
                        val subscriptionPlayerInfo = SubscriptionsPlayersManager.getItem(gameStateDTO.gamePartId!!,
                            gameStateDTO.currentPlayerId!!)
                        if (subscriptionPlayerInfo != null) {
                            gameService.play(gameStateDTO)
                        } else
                            println("subscriptionPlayerInfo is null")
                    } else {
                        println("SubscriptionsPlayersManager doesn't contains gameID ${gameStateDTO.gamePartId}")
                    }
                else
                    println("Id or currentPlayer not found")
            }
        })
    }
}