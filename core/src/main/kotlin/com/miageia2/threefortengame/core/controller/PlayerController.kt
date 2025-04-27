package com.miageia2.threefortengame.core.controller


import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import com.miageia2.threefortengame.common.dto.core.PlayerInfo
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
            ResponseEntity<List<GamePartDTO>> {
        val player = playerService.getPlayerByUsername(username)
        val games = playerService.getGamesByPlayerAndStatus(player.id!!, status)
        val gamePartsDTO = games.map {
            gamePartToDTO(it)
        }
        return ResponseEntity.ok(gamePartsDTO)
    }


    fun gamePartToDTO(gamePart: GamePart): GamePartDTO {
        var player1Info: PlayerInfo? = null
        var player2Info: PlayerInfo? = null
        if (gamePart.player1Id != null) {
            val player =  playerService.findById(gamePart.player1Id!!)
            player1Info = player.toPlayerInfo()
        }
        if (gamePart.player2Id != null) {
            val player =  playerService.findById(gamePart.player2Id!!)
            player2Info = player.toPlayerInfo()
        }
        return gamePart.toGamePartDTO(player1Info, player2Info)
    }

}
