package com.miageia2.threefortengame.aiplayer.dto

import com.miageia2.threefortengame.common.AiPlayerType
data class RegisterGameDTO (
    val gamePartId: String,
    val aiPlayerType: AiPlayerType
)