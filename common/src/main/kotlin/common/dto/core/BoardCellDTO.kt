package com.miageia2.threefortengame.common.dto.core
import com.miageia2.threefortengame.common.utils.core.WinningDirection

data class BoardCellDTO(
    val value: Int = 0,  // Valeur placée par un joueur (1 à 8)
    val wonCasesDirections: MutableSet<WinningDirection> = mutableSetOf()
)
