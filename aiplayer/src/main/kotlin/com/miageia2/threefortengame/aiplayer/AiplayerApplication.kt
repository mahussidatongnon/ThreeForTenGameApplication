package com.miageia2.threefortengame.aiplayer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.socket.config.annotation.EnableWebSocket

//import org.springframework.web.socket.config.annotation.EnableWebSocket

@SpringBootApplication
@EnableFeignClients
@EnableWebSocket
class AiplayerApplication

fun main(args: Array<String>) {
    runApplication<AiplayerApplication>(*args)
}
