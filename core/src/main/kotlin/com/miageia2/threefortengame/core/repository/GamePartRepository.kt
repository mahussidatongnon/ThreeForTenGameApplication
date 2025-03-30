package com.miageia2.threefortengame.core.repository

import com.miageia2.threefortengame.core.entity.GamePart
import org.springframework.data.mongodb.repository.MongoRepository

interface GamePartRepository : MongoRepository<GamePart?, String?> {
}