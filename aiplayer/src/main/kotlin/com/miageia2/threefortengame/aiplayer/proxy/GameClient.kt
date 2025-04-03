package com.miageia2.threefortengame.aiplayer.proxy

import com.miageia2.threefortengame.common.dto.core.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(name = "gameClient", url = "\${core.url}/games")
interface GameClient {
    @GetMapping("/{gameId}")
    fun getById(@PathVariable gameId: String): GamePartDTO

    @PostMapping("/")
    fun createGame(@RequestBody gamePartCreateDTO: GamePartCreateDTO): GamePartDTO

    @PostMapping("/{gameId}/join")
    fun joinGame(@PathVariable gameId: String, @RequestBody gamePartJoinDTO: GamePartJoinDTO): GamePartDTO

    @PostMapping("/{gameId}/start")
    fun startGame(@PathVariable gameId: String): GamePartDTO

    @GetMapping("/{gameId}/state")
    fun getState(@PathVariable gameId: String): GameStateDTO

    @PostMapping("/{gameId}/play")
    fun play(@PathVariable gameId: String, @RequestBody playGameDTO: PlayGameDTO): GameStateDTO

    @GetMapping("/{gameId}/legal-actions")
    fun getLegalActions(@PathVariable gameId: String): Array<PointDTO>

}