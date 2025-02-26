package com.miageia2.threeForTengame.entity

import com.miageia2.threeForTengame.entity.utils.WinningDirection
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "players_turns")
data class PlayerTurn(
    val turn: Int,
    val point: Point,
    val value: Int,
    val gameStateId: String,
    val wonPoints: EnumMap<WinningDirection, MutableSet<Point>>,
)
