package com.miageia2.threefortengame.core.entity.utils

data class BoardCell(
    val value: Int = 0,  // Valeur placée par un joueur (1 à 8)
    val wonCasesDirections: MutableSet<com.miageia2.threefortengame.core.entity.utils.WinningDirection> = mutableSetOf()
)

