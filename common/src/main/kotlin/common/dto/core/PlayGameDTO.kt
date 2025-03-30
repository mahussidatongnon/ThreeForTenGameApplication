package com.miageia2.threefortengame.common.dto.core

data class PlayGameDTO(
    val coordinates: PointDTO,
    val coinValue: Int,
    val playerUsername: String
)
