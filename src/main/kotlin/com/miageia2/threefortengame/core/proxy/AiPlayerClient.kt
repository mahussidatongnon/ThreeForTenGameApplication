package com.miageia2.threefortengame.core.proxy

import org.springframework.cloud.openfeign.FeignClient

@FeignClient("aiplayer-client", url = "http://localhost:8081")
interface AiPlayerClient {

}