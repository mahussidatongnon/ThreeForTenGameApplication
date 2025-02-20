package com.miageia2.threeForTengame.service

import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.repository.PlayerRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService(private val playerRepository: PlayerRepository) {

    fun createPlayer(username: String): Player {
        if (playerRepository.findByUsername(username)!!.isPresent) {
            throw RuntimeException("Username already taken: $username")
        }
        val player = Player()
        player.setUsername(username)
        player.setGamesPlayed(0)
        player.setGamesWon(0)
        return playerRepository.save(player)
    }

    fun getPlayerByUsername(username: String?): Optional<Player?>? {
        return playerRepository.findByUsername(username)
    }
}
