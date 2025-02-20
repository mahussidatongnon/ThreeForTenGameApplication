package com.miageia2.threeForTengame.entity

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Builder
@Data
@Document(collection = "game_parts")
class GamePart {
    @Id
    private val id: String? = null
    private val player1Id: String? = null // Référence vers Player
    private val player2Id: String? = null // Référence vers Player
    private val gameStateId: String? = null
    private val status: String? = null // "waiting", "in_progress", "finished"
    private val winnerId: String? = null // Référence vers Player si gagnant
    private val history: List<String>? = null
    private val createdAt: Instant? = null
    private val updatedAt: Instant? = null
}
