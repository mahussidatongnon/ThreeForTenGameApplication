package com.miageia2.threeForTengame.entity.utils

enum class WinningCase(val description: String? = null) {
    LEFT_HORIZONTAL,
    RIGHT_HORIZONTAL,
    UP_VERTICAL,
    DOWN_VERTICAL,
    UP_RIGHT_DIAGONAL(description = "Diagonal montant droite"),
    UP_LEFT_DIAGONAL(description = "Diagonal montant gauche"),
    DOWN_RIGHT_DIAGONAL,
    DOWN_LEFT_DIAGONAL,
}