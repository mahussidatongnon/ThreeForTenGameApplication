package com.miageia2.threeForTengame.repository

import com.miageia2.threeForTengame.entity.GameState
import org.springframework.data.mongodb.repository.MongoRepository

interface GameStateRepository: MongoRepository<GameState, String> {
}