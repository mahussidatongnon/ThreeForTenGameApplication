package com.miageia2.threefortengame.core.dto

data class PointDTO(
    val x: Int = 0,
    val y: Int = 0,
)
data class PlayGameDTO(
    val coordinates: PointDTO,
    val coinValue: Int,
    val playerUsername: String
)
