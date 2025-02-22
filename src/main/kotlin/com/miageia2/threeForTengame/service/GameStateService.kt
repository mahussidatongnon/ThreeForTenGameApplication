package com.miageia2.threeForTengame.service

import com.miageia2.threeForTengame.entity.GamePart
import com.miageia2.threeForTengame.entity.GameState
import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.repository.GameStateRepository
import org.springframework.stereotype.Service

@Service
class GameStateService(val gameStateRepository: GameStateRepository) {

    fun play(gamePart: GamePart, player: Player): GameState {
        val gameState = GameState()
        // check rules etc ...
        return gameState
    }
}