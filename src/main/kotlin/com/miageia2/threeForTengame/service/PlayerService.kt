package com.miageia2.threeForTengame.service

import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.repository.PlayerRepository
//import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

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

    fun getPlayerByUsername(username: String?): Optional<Player?>? {
        return playerRepository.findByUsername(username)
    }

    fun findAll(): List<Player?> {
        return playerRepository.findAll().toList()
    }
}
