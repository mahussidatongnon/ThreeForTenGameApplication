package com.miageia2.threefortengame.aiplayer.proxy

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import com.miageia2.threefortengame.common.dto.GamePartDTO

@FeignClient(name = "gameClient", url = "http://localhost:8080/games/")
interface GameClient {
    @GetMapping("/users/{id}")
    fun getById(@PathVariable("id") id: String): GamePartDTO
}