package com.miageia2.threeForTengame.entity

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Builder
@Data
@Document(collection = "game_parts")
data class GamePart(
    @Id
    val id: String? = null,
    val player1Id: String? = null,// Référence vers Player
    val player2Id: String? = null, // Référence vers Player
    val gameStateId: String? = null,
    val status: String? = null, // "waiting", "in_progress", "finished"
    val winnerId: String? = null, // Référence vers Player si gagnant
    val history: List<String>? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
