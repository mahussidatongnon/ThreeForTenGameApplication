package com.miageia2.threeForTengame.repository

import com.miageia2.threeForTengame.entity.Player
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface PlayerRepository : MongoRepository<Player?, String?> {
    fun findByUsername(username: String?): Optional<Player?>
}