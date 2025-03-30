package com.miageia2.threefortengame.aiplayer.controller

import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.miageia2.threefortengame.aiplayer.proxy.GameClient
import com.miageia2.threefortengame.aiplayer.service.AIPlayerWebSocketClient
import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.common.dto.core.GamePartCreateDTO
import com.miageia2.threefortengame.common.dto.core.PlayGameDTO

@RestController("/aiplayer")
class GameController(private val gameClient: GameClient) {


    @PostMapping("/register")
    fun register(@RequestBody registerGameDTO: RegisterGameDTO): ResponseEntity<Unit> {
        val gamePart: GamePartDTO = gameClient.getById(registerGameDTO.gamePartId)
        val aiPlayer = AIPlayerWebSocketClient(gamePart.id)
        aiPlayer.connect()
        return ResponseEntity.ok().build()
    }

    @PostMapping("/training")
    fun training(): ResponseEntity<Unit> {
        val secretCode = "123456"
        var gamePart = gameClient.createGame(GamePartCreateDTO(
            player1Username = AiPlayerType.RANDOM.name,
            player2Username = AiPlayerType.RANDOM.name,
            secretCode = secretCode,
            nbCasesCote = 7
        ))
        println("gamePart: $gamePart")
        gameClient.startGame(gamePart.id)
        return ResponseEntity.ok().build()
    }
}