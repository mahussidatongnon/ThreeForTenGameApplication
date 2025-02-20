package com.miageia2.threeForTengame.controller


import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.service.PlayerService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/players")
class PlayerController(private val playerService: PlayerService) {

    @PostMapping("/register")
    fun registerPlayer(@RequestParam username: String?): Player {
        return playerService.createPlayer(username!!)
    }

    @GetMapping("/{username}")
    fun getPlayer(@PathVariable username: String?): Optional<Player?>? {
        return playerService.getPlayerByUsername(username)
    }
}
