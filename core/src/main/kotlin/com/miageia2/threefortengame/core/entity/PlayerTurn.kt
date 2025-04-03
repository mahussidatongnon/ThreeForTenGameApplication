package com.miageia2.threefortengame.core.entity

import com.miageia2.threefortengame.common.utils.core.WinningDirection
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "players_turns")
data class PlayerTurn(
    val turn: Int,
    val point: Point,
    val coinValue: Int,
    val score: Int = 0,
    val gameStateId: String,
    val wonPoints: EnumMap<WinningDirection, MutableSet<Point>>,
)
