package com.miageia2.threefortengame.core.service

import com.miageia2.threefortengame.core.entity.Player
import com.miageia2.threefortengame.core.repository.PlayerRepository
//import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PlayerService(private val playerRepository: PlayerRepository,
//                    val passwordEncoder: PasswordEncoder
) {

    fun createPlayer(username: String, rawPassword: String): Player {
        if (playerRepository.findByUsername(username).isPresent) {
            throw RuntimeException("Username already taken: $username")
        }
//        val password = passwordEncoder.encode(raw_password)
        val player = Player(username=username, password=rawPassword)
        return playerRepository.save(player)
    }

    fun getPlayerByUsername(username: String): Player {
        val player = playerRepository.findByUsername(username)
        if (player.isEmpty)
            throw IllegalArgumentException("Player $username not found")
        return player.get()
    }

    fun findAll(): List<Player?> {
        return playerRepository.findAll().toList()
    }
}
