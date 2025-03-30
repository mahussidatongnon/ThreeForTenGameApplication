package com.miageia2.threefortengame.aiplayer.controller

import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController(value = "")
@RequestMapping("/")
class HelloWorld {
    @GetMapping("/", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun hello(): ResponseEntity<Any> {
//        return ResponseEntity("Hello, World", HttpStatus.OK)
        val mapper = JsonMapper()
        val message = mapper.writeValueAsString(object : Any() {
            @Suppress("unused")
            val message: String = "Hello, World!"
        })
        return ResponseEntity.ok(message)
    }
}