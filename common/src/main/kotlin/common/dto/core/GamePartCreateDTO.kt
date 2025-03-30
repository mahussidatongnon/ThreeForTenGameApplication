package com.miageia2.threefortengame.common.dto.core

data class GamePartCreateDTO(
    val player1Username: String,
    val player2Username: String? = null,
    val secretCode: String,
    val nbCasesCote: Int = 7
)
