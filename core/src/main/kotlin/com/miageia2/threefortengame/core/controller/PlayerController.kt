package com.miageia2.threefortengame.core.controller


import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import com.miageia2.threefortengame.core.entity.Player
import com.miageia2.threefortengame.common.utils.core.GamePartStatus
import com.miageia2.threefortengame.core.entity.GamePart
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

    @GetMapping("/{username}/games")
    fun getPlayerGames(@PathVariable username: String, @RequestParam(required = false) status: GamePartStatus?):
            ResponseEntity<List<GamePart>> {
        val player = playerService.getPlayerByUsername(username)
        val games = playerService.getGamesByPlayerAndStatus(player.id!!, status)
        return ResponseEntity.ok(games)
    }
}
