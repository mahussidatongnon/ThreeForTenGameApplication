package com.miageia2.threeForTengame.entity.utils

data class BoardCell(
    val value: Int = 0,  // Valeur placée par un joueur (1 à 8)
    val wonCasesDirections: MutableSet<WinningDirection> = mutableSetOf()
)

