package com.miageia2.threefortengame.core.controller

import com.miageia2.threefortengame.common.dto.core.PlayerCreateDTO
import com.miageia2.threefortengame.core.entity.Player
import com.miageia2.threefortengame.core.service.PlayerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController( val playerService: PlayerService) {
    @PostMapping("/register", name = "auth.register")
    fun registerPlayer(@RequestBody createPlayerCreateDTO: PlayerCreateDTO): Player {
        return playerService.createPlayer(createPlayerCreateDTO.username, createPlayerCreateDTO.password)
    }
}