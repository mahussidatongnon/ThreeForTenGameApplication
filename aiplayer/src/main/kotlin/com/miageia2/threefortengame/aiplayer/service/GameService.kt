package com.miageia2.threefortengame.aiplayer.service

import com.miageia2.threefortengame.aiplayer.proxy.GameClient
import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import org.springframework.stereotype.Service

@Service
class GameService(private val gameClient: GameClient) {
    fun getGame(gameId: String): GamePartDTO {
        return gameClient.getById(gameId)
    }
}
