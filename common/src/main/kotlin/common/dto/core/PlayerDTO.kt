package com.miageia2.threefortengame.common.dto.core

data class PlayerDTO  (
    val id: String? = null,
    val username: String,
    val password: String? = null,
    var gamesPlayed: Int = 0,
    var gamesWon: Int = 0
)