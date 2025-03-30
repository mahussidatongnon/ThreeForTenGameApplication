package com.miageia2.threefortengame.aiplayer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class AiplayerApplication

fun main(args: Array<String>) {
    runApplication<AiplayerApplication>(*args)
}
