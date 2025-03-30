package com.miageia2.threefortengame.common.dto.aiplayer

import com.miageia2.threefortengame.common.AiPlayerType

data class RegisterGameDTO (
    val gamePartId: String,
    val aiPlayerType: AiPlayerType
)