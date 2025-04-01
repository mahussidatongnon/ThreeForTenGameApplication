package com.miageia2.threefortengame.aiplayer.autorunner

import com.miageia2.threefortengame.aiplayer.service.AIPlayerRegisterWebSocketClient
import com.miageia2.threefortengame.aiplayer.service.GameService
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RegisterNewAiRunner(private val gameService: GameService) {  // Injection correcte de GameService
    @Bean
    fun runAtStartup(): ApplicationRunner {
        return ApplicationRunner {
            println("L'application vient de démarrer !")
            AIPlayerRegisterWebSocketClient(gameService).connect()  // Passage du service correctement
        }
    }
}
