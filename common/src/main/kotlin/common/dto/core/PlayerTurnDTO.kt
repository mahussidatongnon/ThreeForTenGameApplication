package com.miageia2.threefortengame.common.dto.core

import com.miageia2.threefortengame.common.utils.core.WinningDirection
import java.util.*

data class PlayerTurnDTO (
    val turn: Int,
    val point: PointDTO,
    val coinValue: Int,
    val score: Int = 0,
    val gameStateId: String,
    val wonPoints: EnumMap<WinningDirection, MutableSet<PointDTO>>,
)