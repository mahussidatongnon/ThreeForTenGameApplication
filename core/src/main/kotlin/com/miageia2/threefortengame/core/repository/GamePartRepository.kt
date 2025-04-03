package com.miageia2.threefortengame.core.repository

import com.miageia2.threefortengame.common.utils.core.GamePartStatus
import com.miageia2.threefortengame.core.entity.GamePart
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface GamePartRepository : MongoRepository<GamePart?, String?> {
    @Query("{ '\$or': [ { 'player1Id': ?0 }, { 'player2Id': ?0 } ], 'status': ?1 }")
    fun findByPlayerAndStatus(playerId: String, status: GamePartStatus): List<GamePart>

    @Query("{ '\$or': [ { 'player1Id': ?0 }, { 'player2Id': ?0 } ]}")
    fun findByPlayer(playerId: String): List<GamePart>

}