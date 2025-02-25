package com.miageia2.threeForTengame.entity

import com.miageia2.threeForTengame.entity.utils.WinningDirection
import java.util.*

data class PlayerTurn(
    val point: Point,
    val value: Int,
    val wonPoints: EnumMap<WinningDirection, MutableSet<Point>>? = null
)
