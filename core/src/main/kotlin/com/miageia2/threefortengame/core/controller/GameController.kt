package com.miageia2.threefortengame.core.controller


import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import com.miageia2.threefortengame.common.dto.core.GamePartCreateDTO
import com.miageia2.threefortengame.common.dto.core.GamePartJoinDTO
import com.miageia2.threefortengame.common.dto.core.PlayGameDTO
import com.miageia2.threefortengame.common.dto.core.PointDTO
import com.miageia2.threefortengame.core.entity.GamePart
import com.miageia2.threefortengame.core.entity.GameState
import com.miageia2.threefortengame.core.service.GamePartService
import com.miageia2.threefortengame.core.service.PlayerService
import com.miageia2.threefortengame.core.service.GameStateService
import com.miageia2.threefortengame.core.service.WebSocketService
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
class GameController(private val gamePartService: GamePartService,
                     private val playerService: PlayerService,
                     private val gameStateService:GameStateService,
                     private val websocketService: WebSocketService,
    ) {

    @PostMapping("/")
    fun createGame(@RequestBody gamePartCreateDTO: GamePartCreateDTO): ResponseEntity<GamePart> {
        val gamePart = gamePartService.createGame(gamePartCreateDTO)
        arrayOf(gamePart.player1Id, gamePart.player2Id).forEachIndexed() { index, playerId ->
            playerId?.let {
                val player = playerService.findById(it)
                if (player.username in AiPlayerType.entries.map { it.name }) {
                    println("Register: $it")
                    val registerGameDTO = RegisterGameDTO(gamePart.id!!, AiPlayerType.valueOf(player.username),
                        player.id!!)
                    websocketService.sendMessage("/players.register", registerGameDTO)
                    println("Demande d'enrégistrement player_${index+1}-createGame")
                }
            }
        }
        return ResponseEntity(gamePart, HttpStatus.CREATED)
    }

    @PostMapping("/{gameId}/join")
    fun joinGame(@PathVariable gameId: String, @RequestBody gamePartJoinDTO: GamePartJoinDTO): ResponseEntity<GamePart> {
        var gamePart = gamePartService.findById(gameId)
        gamePart = gamePartService.joinGame(gamePart, gamePartJoinDTO)
        gamePart.player2Id?.let {
            val player = playerService.findById(it)
            if (player.username in AiPlayerType.entries.map { it.name }) {
                val registerGameDTO = RegisterGameDTO(gamePart.id!!, AiPlayerType.valueOf(player.username),
                    player.id!!)
                websocketService.sendMessage("/players.register", registerGameDTO)
                println("Demande d'enrégistrement player2-joinGame")
            }
        }
        return ResponseEntity(gamePart, HttpStatus.OK)
    }

    @GetMapping("/{gameId}")
    fun findById(@PathVariable gameId: String): ResponseEntity<GamePart> {
        return ResponseEntity(gamePartService.findById(gameId), HttpStatus.OK)
    }

//    @GetMapping("/debug")
//    fun debug(): ResponseEntity<Unit> {
//        val registerGameDTO = RegisterGameDTO("67ebba24d009911e746e7983", AiPlayerType.RANDOM_AI)
//        websocketService.sendMessage("/players.register", registerGameDTO)
//        println("Message envoyé avec success")
//        return ResponseEntity.ok().build()
//    }

    @GetMapping("")
    fun findAll(): ResponseEntity<List<GamePart?>> {
        return ResponseEntity(gamePartService.findAll(), HttpStatus.OK)
    }

    @PostMapping("/{gameId}/start")
    fun startGame(@PathVariable gameId: String): ResponseEntity<GamePart> {
        val gamePart = gamePartService.startGame(gameId)
        val gameState = gameStateService.findById(gamePart.gameStateId!!)
        websocketService.sendMessage("/games.${gamePart.id}.state", gameState)
        return ResponseEntity.ok(gamePart)
    }

    @PostMapping("/{gameId}/play")
    fun play(@PathVariable gameId: String, @RequestBody playGameDTO: PlayGameDTO): ResponseEntity<GameState> {
        val gamePart = gamePartService.findById(gameId)

        if (gamePart.gameStateId.isNullOrBlank())
            throw IllegalArgumentException("GamePart not found with id: $gameId")

        val player = playerService.getPlayerByUsername(playGameDTO.playerUsername)

        val gameState = gameStateService.play(gamePart, player, playGameDTO.coordinates, playGameDTO.coinValue)

        websocketService.sendMessage("/games.${gamePart.id}.state", gameState)
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
