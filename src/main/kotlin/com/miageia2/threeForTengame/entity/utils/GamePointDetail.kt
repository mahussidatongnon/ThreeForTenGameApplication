package com.miageia2.threeForTengame.entity.utils

data class GamePointDetail(
    val playerId: String,
    val winningCases: List<WinningCase?> = mutableListOf(),
)
