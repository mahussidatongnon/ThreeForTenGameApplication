package com.miageia2.threefortengame.aiplayer.proxy

import com.miageia2.threefortengame.common.dto.core.GamePartDTO
import com.miageia2.threefortengame.common.dto.core.PlayerDTO
import com.miageia2.threefortengame.common.utils.core.GamePartStatus
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "playerClient", url = "\${core.url}/players")
interface PlayerClient {
    @GetMapping("/{username}")
    fun getPlayer(@PathVariable username: String): PlayerDTO

    @GetMapping("/{username}/games")
    fun getPlayerGames(@PathVariable username: String, @RequestParam(required = false) status: GamePartStatus?):
            List<GamePartDTO>
}