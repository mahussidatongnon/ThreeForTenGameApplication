package com.miageia2.threefortengame.aiplayer.autorunner

import com.miageia2.threefortengame.aiplayer.service.GameService
import com.miageia2.threefortengame.aiplayer.service.SubscribeRegisterPlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RegisterNewAiRunner(@Autowired private val subscribeRegisterPlayerService: SubscribeRegisterPlayerService) {  // Injection correcte de GameService
    @Bean
    fun runRegisterNewAiRunnerAtStartup(): ApplicationRunner {
        return ApplicationRunner {
            println("L'application vient de démarrer !")
            subscribeRegisterPlayerService.handle()
        }
    }
}
