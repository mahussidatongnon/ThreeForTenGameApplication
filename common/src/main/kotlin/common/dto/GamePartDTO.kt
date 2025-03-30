package com.miageia2.threefortengame.common.dto

import com.miageia2.threefortengame.common.utils.GamePartStatus
import java.time.Instant

data class GamePartDTO(
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
