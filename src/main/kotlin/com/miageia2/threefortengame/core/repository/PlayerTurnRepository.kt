package com.miageia2.threefortengame.core.repository

import com.miageia2.threefortengame.core.entity.PlayerTurn
import org.springframework.data.mongodb.repository.MongoRepository

interface PlayerTurnRepository: MongoRepository<PlayerTurn, String> {
}