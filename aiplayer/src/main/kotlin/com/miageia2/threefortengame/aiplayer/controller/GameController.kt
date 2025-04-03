package com.miageia2.threefortengame.aiplayer.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import com.miageia2.threefortengame.aiplayer.proxy.GameClient
import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.common.dto.core.GamePartCreateDTO

@RestController("/aiplayer")
class GameController(private val gameClient: GameClient) {


    @PostMapping("/training")
    fun training(): ResponseEntity<Unit> {
        val secretCode = "123456"
        var gamePart = gameClient.createGame(GamePartCreateDTO(
            player1Username = AiPlayerType.RANDOM.name,
            player2Username = AiPlayerType.RANDOM.name,
            secretCode = secretCode,
            nbCasesCote = 7
        ))
        gameClient.startGame(gamePart.id)
        return ResponseEntity.ok().build()
    }
}