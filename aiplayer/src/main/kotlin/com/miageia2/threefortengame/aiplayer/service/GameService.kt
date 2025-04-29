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
import com.miageia2.threefortengame.aiplayer.service.agent.QValueStore
import com.miageia2.threefortengame.aiplayer.service.agent.toState
import java.io.File

@Service
class GameService(private val gameClient: GameClient, private val qValuesStore: QValueStore) {

    fun distance(p1: PointDTO, p2: PointDTO): Int {
        return kotlin.math.abs(p1.x - p2.x) + kotlin.math.abs(p1.y - p2.y) // distance de Manhattan
    }

    fun getGame(gameId: String): GamePartDTO {
        return gameClient.getById(gameId)
    }

    fun play(gameStateDTO: GameStateDTO, aiPlayerType: AiPlayerType = AiPlayerType.RANDOM_AI) {
        println("Playing ${gameStateDTO.id}")
        val subscriptionPlayerInfo = SubscriptionsPlayersManager.getItem(gameID = gameStateDTO.gamePartId!!,
            playerId = gameStateDTO.currentPlayerId!!)
        val action: Action?

        println("game: ${gameStateDTO.gamePartId} AI: $aiPlayerType")
        when (aiPlayerType) {
            AiPlayerType.RANDOM_AI -> {
                val legalActions: Array<PointDTO> = gameClient.getLegalActions(gameStateDTO.gamePartId!!)
                val coordinates = legalActions.random()
                action = Action(coordinates, (1..8).random())
            }
            AiPlayerType.PASSIF_MOST_AWAY_CONNER_AI -> {
                val legalActions: Array<PointDTO> = gameClient.getLegalActions(gameStateDTO.gamePartId!!)
                val size = gameStateDTO.boardState!!.size
                val corners = listOf(
                    PointDTO(0, 0),
                    PointDTO(0, size - 1),
                    PointDTO(size - 1, 0),
                    PointDTO(size - 1, size - 1)
                )
                val coordinates = legalActions.maxBy { point ->
                    corners.minOf { corner -> distance(point, corner) } // distance au coin le plus proche
                }
                action = Action(coordinates, (1..8).random())
            }
            AiPlayerType.ACTIF_AI -> {
                val agent = QLearningAgent(startState = gameStateDTO.boardState!!.toState())
                val size = agent.startState.size
//                val filePath = when {
//                    size <= 5 -> "/app/qValues/5Best.jsonl"
//                    else -> "/app/qValues/${size}Best.jsonl"
//                }
                val qValues = when {
                    size <= 5 -> qValuesStore.getQValues(5)
                    else -> qValuesStore.getQValues(size)
                }
                agent.loadQValues(qValues!!)
//                println("filePath: $filePath")
//                println("File exists: ${File(filePath).exists()}")
//                println("File exists sans app: ${File(filePath).exists()}")
//                agent.loadQValues(filePath = filePath)
                action = agent.getPolicy(gameStateDTO.boardState!!.toState())
                println("agent: $agent, action: $action")
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
