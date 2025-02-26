package com.miageia2.threeForTengame.repository

import com.miageia2.threeForTengame.entity.PlayerTurn
import org.springframework.data.mongodb.repository.MongoRepository

interface PlayerTurnRepository: MongoRepository<PlayerTurn, String> {
}