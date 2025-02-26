package com.miageia2.threeForTengame.controller

import com.miageia2.threeForTengame.dto.PlayerCreateDTO
import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.service.PlayerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/auth")
class AuthController( val playerService: PlayerService) {
    @PostMapping("/register", name = "auth.register")
    fun registerPlayer(@RequestBody createPlayerCreateDTO: PlayerCreateDTO): Player {
        return playerService.createPlayer(createPlayerCreateDTO.username, createPlayerCreateDTO.password)
    }
}