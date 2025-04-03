package com.miageia2.threefortengame.aiplayer.autorunner

import com.miageia2.threefortengame.aiplayer.proxy.GameClient
import com.miageia2.threefortengame.aiplayer.proxy.PlayerClient
import com.miageia2.threefortengame.aiplayer.service.GameService
import com.miageia2.threefortengame.aiplayer.service.SubscribeGameStateService
import com.miageia2.threefortengame.aiplayer.utils.SubscriptionPlayerInfo
import com.miageia2.threefortengame.aiplayer.utils.SubscriptionsPlayersManager
import com.miageia2.threefortengame.common.AiPlayerType
import com.miageia2.threefortengame.common.utils.core.GamePartStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StartedGamesWatcherRunner(val gameClient: GameClient, val playerClient: PlayerClient,
                                val subscribeGameStateService: SubscribeGameStateService,
                                val gameService: GameService
    ) {
    private val logger: Logger = LoggerFactory.getLogger(StartedGamesWatcherRunner::class.java)

    @Bean
    fun runStartedGamesWatcherRunnerAtStartup(): ApplicationRunner {
        return ApplicationRunner {
            logger.info("Starting game watcher for started games")
            println("L'application vient de démarrer !")
            AiPlayerType.entries.forEach {
                println("AI: $it")
                val player = playerClient.getPlayer(it.name)
                val games = playerClient.getPlayerGames(it.name, GamePartStatus.STARTED)
                games.forEach { game ->
                    println("Game: ${game.id}")
                    SubscriptionsPlayersManager.addItem(game.id, SubscriptionPlayerInfo(
                        playerId = player.id!!,
                        aiPlayerType = AiPlayerType.valueOf(it.name))
                    )
                    subscribeGameStateService.handle(game.id)
                    val gameState = gameClient.getState(game.id)
                    if (gameState.currentPlayerId == player.id) {
                        println("C'est mon tour de jouer")
                        gameService.play(gameState)
                    } else
                        println("pas mon tour")
                }

            }
        }
    }
}