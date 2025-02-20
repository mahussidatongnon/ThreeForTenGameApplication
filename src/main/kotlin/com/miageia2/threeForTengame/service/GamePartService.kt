package com.miageia2.threeForTengame.service


import com.miageia2.threeForTengame.entity.GamePart
import com.miageia2.threeForTengame.repository.GamePartRepository
import com.miageia2.threeForTengame.repository.PlayerRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GamePartService(val gamePartRepository: GamePartRepository, val playerRepository: PlayerRepository) {

    fun createGame(player1Username: String, player2Username: String): GamePart {
        val player1 = playerRepository.findByUsername(player1Username)
            ?.orElseThrow { RuntimeException("Player not found: $player1Username") }!!
        val player2 = playerRepository.findByUsername(player2Username)
            ?.orElseThrow { RuntimeException("Player not found: $player2Username") }!!

        val game = GamePart()
        game.setPlayer1Id(player1.getId())
        game.setPlayer2Id(player2.getId())
        game.setStatus("waiting")
        game.setCreatedAt(Instant.now())
        game.setUpdatedAt(Instant.now())

        return gamePartRepository.save(game)
    }
}
