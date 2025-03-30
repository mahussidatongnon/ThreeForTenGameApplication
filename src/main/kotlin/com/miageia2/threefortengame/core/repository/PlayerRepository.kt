package com.miageia2.threefortengame.core.repository

import com.miageia2.threefortengame.core.entity.Player
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface PlayerRepository : MongoRepository<Player?, String?> {
    fun findByUsername(username: String?): Optional<Player>
}