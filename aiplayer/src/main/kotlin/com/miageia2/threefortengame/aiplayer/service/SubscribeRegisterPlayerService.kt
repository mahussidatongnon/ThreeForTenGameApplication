package com.miageia2.threefortengame.aiplayer.service

import com.miageia2.threefortengame.aiplayer.utils.SubscriptionPlayerInfo
import com.miageia2.threefortengame.aiplayer.utils.SubscriptionsPlayersManager
import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.stomp.*
import org.springframework.stereotype.Service
import java.lang.reflect.Type

@Service
class SubscribeRegisterPlayerService(private val gameService: GameService, private val webSocketService: WebSocketService) {

    @Autowired
    private lateinit var subscribeGameStateService: SubscribeGameStateService

    fun handle() {
        webSocketService.stompSession.subscribe("/topic/players.register", object : StompFrameHandler {
            override fun getPayloadType(headers: StompHeaders): Type {
                return String::class.java
            }

            override fun handleFrame(headers: StompHeaders, payload: Any?) {
                println("📩 With topic Notification reçue : $payload")
                val registerGameDTO = webSocketService.jsonMapperObject.readValue(payload as String, RegisterGameDTO::class.java)
                if (registerGameDTO != null)
                    println("Type: ${registerGameDTO::class}")

//                             🔹 Récupération du jeu et connexion de l'IA
                val gamePart: GamePartDTO = gameService.getGame(registerGameDTO.gamePartId)
                SubscriptionsPlayersManager.addItem(gamePart.id, SubscriptionPlayerInfo(
                    playerId = registerGameDTO.playerId,
                    aiPlayerType = registerGameDTO.aiPlayerType,
                ))
                subscribeGameStateService.handle(gamePart.id)
            }
        })
    }
}
