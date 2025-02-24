package com.miageia2.threeForTengame.service.utils

import com.miageia2.threeForTengame.entity.Point
import com.miageia2.threeForTengame.entity.utils.WinningCase

data class WinnablePoint (
    val point1: Point,
    val point2: Point,
    val winningCase: WinningCase
)