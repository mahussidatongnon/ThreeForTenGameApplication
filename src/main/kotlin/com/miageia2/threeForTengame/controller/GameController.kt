package com.miageia2.threeForTengame.controller


import com.miageia2.threeForTengame.dto.GamePartCreateDTO
import com.miageia2.threeForTengame.dto.GamePartJoinDTO
import com.miageia2.threeForTengame.dto.PlayGameDTO
import com.miageia2.threeForTengame.entity.GamePart
import com.miageia2.threeForTengame.entity.GameState
import com.miageia2.threeForTengame.service.GamePartService
import com.miageia2.threeForTengame.service.GameStateService
import com.miageia2.threeForTengame.service.PlayerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController( val gamePartService: GamePartService, val playerService: PlayerService,
        val gameStateService: GameStateService
    ) {

    @PostMapping("/create")
    fun createGame(@RequestBody gamePartCreateDTO: GamePartCreateDTO): ResponseEntity<GamePart> {
        val gamePart = gamePartService.createGame(gamePartCreateDTO)
        return ResponseEntity(gamePart, HttpStatus.CREATED)
    }

    @PostMapping("/{gameId}/join")
    fun joinGame(@PathVariable gameId: String, @RequestBody gamePartJoinDTO: GamePartJoinDTO): ResponseEntity<GamePart> {
        var gamePart = gamePartService.findById(gameId)
        gamePart = gamePartService.joinGame(gamePart, gamePartJoinDTO)
        return ResponseEntity(gamePart, HttpStatus.OK)
    }

    @GetMapping("/{gameId}")
    fun findById(@PathVariable gameId: String): ResponseEntity<GamePart> {
        return ResponseEntity(gamePartService.findById(gameId), HttpStatus.OK)
    }

    @GetMapping("")
    fun findAll(): ResponseEntity<List<GamePart?>> {
        return ResponseEntity(gamePartService.findAll(), HttpStatus.OK)
    }

    @GetMapping("/{gameId}/start")
    fun startGame(@PathVariable gameId: String): ResponseEntity<GamePart> {
        return ResponseEntity.ok(gamePartService.startGame(gameId))
    }

    @PostMapping("/{gameId}/play")
    fun play(@PathVariable gameId: String, @RequestBody playGameDTO: PlayGameDTO): ResponseEntity<GameState> {
        val gamePart = gamePartService.findById(gameId)

        if (gamePart.gameStateId?.isNotBlank() == true)
            throw IllegalArgumentException("GameState not found with id: $gameId")

        val player = playerService.getPlayerByUsername(playGameDTO.playerUsername)?.orElseThrow { IllegalArgumentException("Username not found") }!!

        return ResponseEntity.ok(gameStateService.play(gamePart, player, playGameDTO.coordinates, playGameDTO.coinValue))
    }
    @GetMapping("/{gameId}/state")
    fun getState(@PathVariable gameId: String): ResponseEntity<GameState> {
        val gamePart = gamePartService.findById(gameId)

        if (gamePart.gameStateId?.isNotBlank() == true)
            throw IllegalArgumentException("GameState not found with id: $gameId")

        val gameState = gameStateService.findById(gamePart.gameStateId!!)
        return ResponseEntity.ok(gameState)
    }
}
