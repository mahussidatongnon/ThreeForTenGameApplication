package com.miageia2.threeForTengame.repository

import com.miageia2.threeForTengame.entity.GamePart
import org.springframework.data.mongodb.repository.MongoRepository

interface GamePartRepository : MongoRepository<GamePart?, String?> {
}