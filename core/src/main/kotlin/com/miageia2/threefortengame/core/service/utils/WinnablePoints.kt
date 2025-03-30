package com.miageia2.threefortengame.core.service.utils

import com.miageia2.threefortengame.core.entity.Point
import com.miageia2.threefortengame.core.entity.utils.WinningCase

data class WinnablePoint (
    val point1: Point,
    val point2: Point,
    val winningCase: WinningCase
)