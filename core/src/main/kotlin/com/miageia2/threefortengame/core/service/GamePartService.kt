package com.miageia2.threefortengame.core.service


import com.miageia2.threefortengame.common.dto.core.GamePartCreateDTO
import com.miageia2.threefortengame.common.dto.core.GamePartJoinDTO
import com.miageia2.threefortengame.core.entity.GamePart
import com.miageia2.threefortengame.core.entity.GameState
import com.miageia2.threefortengame.core.entity.utils.GamePartStatus
import com.miageia2.threefortengame.core.repository.GameStateRepository
import com.miageia2.threefortengame.core.repository.GamePartRepository
import com.miageia2.threefortengame.core.repository.PlayerRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GamePartService(
    private val gamePartRepository: GamePartRepository,
    private val playerRepository: PlayerRepository,
    private val gameStateRepository: GameStateRepository
) {

    fun createGame(gamePartCreateDTO: GamePartCreateDTO): GamePart {
        val player1 = playerRepository.findByUsername(gamePartCreateDTO.player1Username)
            .orElseThrow { RuntimeException("Player not found: ${gamePartCreateDTO.player1Username}") }!!
        val player2 = playerRepository.findByUsername(gamePartCreateDTO.player2Username)

        if ( (2 > gamePartCreateDTO.nbCasesCote) or ( gamePartCreateDTO.nbCasesCote > 7) )
            throw RuntimeException("nbCasesCote must be between 2 and 7")

        val game = GamePart(
            player1Id = player1.id,
            player2Id = if (player2.isEmpty) {null} else {player2.get().id},
            status = GamePartStatus.WAITING,
            secretCode = gamePartCreateDTO.secretCode,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            nbCasesCote = gamePartCreateDTO.nbCasesCote,
        )

        return gamePartRepository.save(game)
    }

    fun joinGame(gamePart: GamePart, gamePartJoinDTO: GamePartJoinDTO): GamePart {
        val player2 = playerRepository.findByUsername(gamePartJoinDTO.playerUsername)
            .orElseThrow { RuntimeException("Player not found: ${gamePartJoinDTO.playerUsername}") }!!

        if (gamePart.status != GamePartStatus.WAITING)
            throw RuntimeException("Game must be waiting for joining. Status: '${gamePart.status}'")

        if (!gamePart.player2Id.isNullOrEmpty())
            throw RuntimeException("Player 2 ID already exists.")

        if (gamePart.player1Id.equals(player2.id))
            throw RuntimeException("You can't play against yourself.")

        if (!gamePart.secretCode.equals(gamePartJoinDTO.secretCode))
            throw RuntimeException("Secret code is not correct.")

        gamePart.player2Id = player2.id
        return gamePartRepository.save(gamePart)
    }

    fun findById(gameId: String): GamePart {
        return gamePartRepository.findById(gameId).orElseThrow { RuntimeException("Game not found: ${gameId}") }!!
    }

    fun findAll(): List<GamePart?> {
        val gameParts = gamePartRepository.findAll()
        return gameParts.toList()
    }

    fun startGame(gameId: String): GamePart {
        val gamePart =
            gamePartRepository.findById(gameId).orElseThrow { RuntimeException("Game not found: ${gameId}") }!!
        if (gamePart.status != GamePartStatus.WAITING)
            throw RuntimeException("Game must be waiting for starting. Status: '${gamePart.status}'")
        if (gamePart.player2Id.isNullOrBlank())
            throw RuntimeException("Player 2 is missing!")

        var gameState = GameState(
            gamePartId = gamePart.id,
            turn = 0,
            currentPlayerId = gamePart.player1Id,
            boardState = Array(gamePart.nbCasesCote) {
                arrayOfNulls<com.miageia2.threefortengame.core.entity.utils.BoardCell?>(gamePart.nbCasesCote)
            },
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

        gameState = gameStateRepository.save(gameState)

        gamePart.gameStateId = gameState.id
        gamePart.status = GamePartStatus.STARTED

        // Create GameState with dimension
        return gamePartRepository.save(gamePart)
    }
}
