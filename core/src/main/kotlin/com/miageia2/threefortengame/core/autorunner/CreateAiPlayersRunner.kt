package com.miageia2.threefortengame.core.autorunner

import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.core.entity.Player
import com.miageia2.threefortengame.core.repository.PlayerRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Optional
import org.slf4j.Logger

@Configuration
class CreateAiPlayersRunner(val playerRepository: PlayerRepository) {
    private val logger: Logger = LoggerFactory.getLogger(CreateAiPlayersRunner::class.java)

    @Bean
    fun runAtStartup(): ApplicationRunner {
        return ApplicationRunner {
            AiPlayerType.entries.forEach {
                val player: Optional<Player> = playerRepository.findByUsername(it.name)
                if (!player.isPresent) {
                    val newPlayer = Player(username=it.name, password=it.name)
                    playerRepository.save(newPlayer)
                    logger.info("AiPlayer ${it.name} created.")
                }
            }
        }
    }
}