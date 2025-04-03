package com.miageia2.threefortengame.common.dto.core

import com.miageia2.threefortengame.common.utils.core.GamePartStatus
import java.time.Instant

data class GamePartDTO(
    var id: String,
    var player1Id: String,// Référence vers Player
    var player2Id: String, // Référence vers Player
    var gameStateId: String? = null,
    var status: GamePartStatus = GamePartStatus.WAITING, // "waiting", "in_progress", "finished"
    var winnerId: String? = null, // Référence vers Player si gagnant
    var history: List<String>? = null,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    val secretCode: String? = null,
    val nbCasesCote: Int = 7,
)
