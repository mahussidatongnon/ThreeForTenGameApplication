package com.miageia2.threefortengame.core.controller


import com.miageia2.threefortengame.core.dto.GamePartCreateDTO
import com.miageia2.threefortengame.core.dto.GamePartJoinDTO
import com.miageia2.threefortengame.core.dto.PlayGameDTO
import com.miageia2.threefortengame.core.dto.PointDTO
import com.miageia2.threefortengame.core.entity.GamePart
import com.miageia2.threefortengame.core.entity.GameState
import com.miageia2.threefortengame.core.service.GamePartService
import com.miageia2.threefortengame.core.service.PlayerService
import com.miageia2.threefortengame.core.service.GameStateService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController( private val gamePartService: GamePartService, private val playerService: PlayerService,
                  private val gameStateService:GameStateService, private val messagingTemplate: SimpMessagingTemplate
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

    @PostMapping("/{gameId}/start")
    fun startGame(@PathVariable gameId: String): ResponseEntity<GamePart> {
        return ResponseEntity.ok(gamePartService.startGame(gameId))
    }

    @PostMapping("/{gameId}/play")
    fun play(@PathVariable gameId: String, @RequestBody playGameDTO: PlayGameDTO): ResponseEntity<GameState> {
        val gamePart = gamePartService.findById(gameId)

        if (gamePart.gameStateId.isNullOrBlank())
            throw IllegalArgumentException("GamePart not found with id: $gameId")

        val player = playerService.getPlayerByUsername(playGameDTO.playerUsername)

        val gameState = gameStateService.play(gamePart, player, playGameDTO.coordinates, playGameDTO.coinValue)

        messagingTemplate.convertAndSend("/topic/gameState/${gameState.id}", gameState)
        return ResponseEntity.ok(gameState)
    }
    @GetMapping("/{gameId}/state")
    fun getState(@PathVariable gameId: String): ResponseEntity<GameState> {
        val gamePart = gamePartService.findById(gameId)

        if (gamePart.gameStateId.isNullOrBlank())
            throw IllegalArgumentException("GamePart not found with id: $gameId")

        val gameState = gameStateService.findById(gamePart.gameStateId!!)
        return ResponseEntity.ok(gameState)
    }

    @GetMapping("/{gameId}/legal-actions")
    fun getLegalActions(@PathVariable gameId: String): ResponseEntity<Array<PointDTO>> {
        val gamePart = gamePartService.findById(gameId)

        if (gamePart.gameStateId.isNullOrBlank())
            throw IllegalArgumentException("GamePart not found with id: $gameId")

        val legalActions = gameStateService.getLegalAction(gamePart.gameStateId!!)
        return ResponseEntity.ok(legalActions)
    }
}
