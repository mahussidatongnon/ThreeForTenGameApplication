package com.miageia2.threefortengame.aiplayer.service

import com.miageia2.threefortengame.aiplayer.proxy.GameClient
import com.miageia2.threefortengame.aiplayer.service.agent.QLearningAgent
import com.miageia2.threefortengame.aiplayer.utils.SubscriptionsPlayersManager
import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import com.miageia2.threefortengame.common.dto.core.GameStateDTO
import com.miageia2.threefortengame.common.dto.core.PlayGameDTO
import com.miageia2.threefortengame.common.dto.core.PointDTO
import org.springframework.stereotype.Service
import com.miageia2.threefortengame.aiplayer.service.agent.Action

@Service
class GameService(private val gameClient: GameClient) {
    fun getGame(gameId: String): GamePartDTO {
        return gameClient.getById(gameId)
    }

    fun play(gameStateDTO: GameStateDTO, aiPlayerType: AiPlayerType = AiPlayerType.RANDOM_AI) {
        println("Playing ${gameStateDTO.id}")
        val subscriptionPlayerInfo = SubscriptionsPlayersManager.getItem(gameID = gameStateDTO.gamePartId!!,
            playerId = gameStateDTO.currentPlayerId!!)
        val action: Action?

        when (aiPlayerType) {
            AiPlayerType.RANDOM_AI -> {
                val legalActions: Array<PointDTO> = gameClient.getLegalActions(gameStateDTO.gamePartId!!)
                val coordinates = legalActions[0]
                action = Action(coordinates,3)
            }
            AiPlayerType.PASSIF_MOST_AWAY_CONNER_AI -> {
                val legalActions: Array<PointDTO> = gameClient.getLegalActions(gameStateDTO.gamePartId!!)
                val coordinates = legalActions[0]
                action = Action(coordinates,3)
            }
            AiPlayerType.ACTIF_AI -> {
                val agent = QLearningAgent(startState = gameStateDTO.boardState!!)
                agent.loadQValues(filePath = "qValues/${agent.startState.size}SquareCases/Best.json")
                action = agent.getPolicy(gameStateDTO.boardState!!)
            }
        }
//        val legalActions: Array<PointDTO> = gameClient.getLegalActions(gameStateDTO.gamePartId!!)
        if (action != null) {
            if (subscriptionPlayerInfo != null) {
                println("Playing ${gameStateDTO.id} executed")
                gameClient.play(gameStateDTO.gamePartId!!,
                    PlayGameDTO(
                        coordinates = action.coordinates,
                        coinValue = action.coinValue,
                        playerUsername = subscriptionPlayerInfo.aiPlayerType.name
                    ))
            } else
                println("Subscription player info is null")
        } else
            println("No legal actions found")
    }

}
