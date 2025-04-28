package com.miageia2.threefortengame.aiplayer.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import com.miageia2.threefortengame.aiplayer.proxy.GameClient
import com.miageia2.threefortengame.aiplayer.service.agent.Agent
import com.miageia2.threefortengame.aiplayer.service.agent.QLearningAgent
import com.miageia2.threefortengame.aiplayer.service.agent.toState
import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.common.dto.core.BoardCellDTO
import com.miageia2.threefortengame.common.dto.core.GamePartCreateDTO
import org.springframework.web.bind.annotation.RequestBody

data class TrainingBody(
    val nbCasesCote: Int = 7,
    val numEpisodes: Int = 1000,
    val alpha: Double = 1.0,
    val epsilon: Double = 0.5,
    val gamma: Double = 0.8
    )
@RestController("/aiplayer")
class GameController(private val gameClient: GameClient) {



    @PostMapping("/training")
    fun training(@RequestBody trainingBody: TrainingBody): ResponseEntity<Unit> {
//            val secretCode = "123456"
//            var gamePart = gameClient.createGame(GamePartCreateDTO(
//                player1Username = AiPlayerType.RANDOM_AI.name,
//                player2Username = AiPlayerType.RANDOM_AI.name,
//                secretCode = secretCode,
//                nbCasesCote = 7
//            ))
//            gameClient.startGame(gamePart.id)
        val nbCasesCote = trainingBody.nbCasesCote
        val initialState = Array(nbCasesCote) {
            arrayOfNulls<BoardCellDTO?>(nbCasesCote)
        }
        val agent = QLearningAgent(
            initialState.toState(),
            alpha=trainingBody.alpha,
            epsilon=trainingBody.epsilon,
            gamma=trainingBody.gamma)
        Agent.train(agent, numEpisodes=trainingBody.numEpisodes)
        return ResponseEntity.ok().build()
    }
}