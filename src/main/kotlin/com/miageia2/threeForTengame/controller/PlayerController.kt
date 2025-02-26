package com.miageia2.threeForTengame.controller


import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.service.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

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
