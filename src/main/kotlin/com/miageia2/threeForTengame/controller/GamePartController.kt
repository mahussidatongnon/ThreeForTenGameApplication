package com.miageia2.threeForTengame.controller


import com.miageia2.threeForTengame.entity.GamePart
import com.miageia2.threeForTengame.service.GamePartService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameController( val gamePartService: GamePartService) {

    @PostMapping("/create")
    fun createGame(@RequestParam player1: String?, @RequestParam player2: String?): GamePart {
        if (player1.isNullOrBlank() || player2.isNullOrBlank()) {
            throw IllegalArgumentException("Player 1 or Player 2 should not be blank")
        }
        return gamePartService.createGame(player1, player2)
    }
}
