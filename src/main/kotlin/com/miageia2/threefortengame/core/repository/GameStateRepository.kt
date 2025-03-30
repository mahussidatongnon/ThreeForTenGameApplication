package com.miageia2.threefortengame.core.repository

import com.miageia2.threefortengame.core.entity.GameState
import org.springframework.data.mongodb.repository.MongoRepository

interface GameStateRepository: MongoRepository<GameState, String> {
}