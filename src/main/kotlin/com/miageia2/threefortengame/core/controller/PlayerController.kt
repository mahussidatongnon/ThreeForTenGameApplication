package com.miageia2.threefortengame.core.controller


import com.miageia2.threefortengame.core.entity.Player
import com.miageia2.threefortengame.core.service.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/players")
class PlayerController(private val playerService: PlayerService) {

    @GetMapping("/{username}")
    fun getPlayer(@PathVariable username: String): Player {
        return playerService.getPlayerByUsername(username)
    }

    @GetMapping("")
    fun getPlayers(): ResponseEntity<List<Player?>> {
        return ResponseEntity.ok(playerService.findAll())
    }
}
