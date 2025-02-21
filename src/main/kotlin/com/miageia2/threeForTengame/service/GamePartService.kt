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

        val game = GamePart(
            player1Id = player1.id,
            player2Id = player2.id,
            status = "waiting",
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        return gamePartRepository.save(game)
    }
}
