package com.miageia2.threefortengame.core.entity

import com.miageia2.threefortengame.core.entity.utils.GamePartStatus
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


@Data
@Document(collection = "game_parts")
data class GamePart(
    @Id
    var id: String? = null,
    var player1Id: String? = null,// Référence vers Player
    var player2Id: String? = null, // Référence vers Player
    var gameStateId: String? = null,
    var status: GamePartStatus = GamePartStatus.WAITING, // "waiting", "in_progress", "finished"
    var winnerId: String? = null, // Référence vers Player si gagnant
    var history: List<String>? = null,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    val secretCode: String? = null,
    val nbCasesCote: Int = 7,
)
