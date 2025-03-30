package com.miageia2.threefortengame.core.proxy

import com.miageia2.threefortengame.common.dto.aiplayer.RegisterGameDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("aiplayer-client", url = "http://localhost:8081/aiplayer")
interface AiPlayerClient {

    @PostMapping("/register/")
    fun register(@RequestBody registerGameDTO: RegisterGameDTO)
}