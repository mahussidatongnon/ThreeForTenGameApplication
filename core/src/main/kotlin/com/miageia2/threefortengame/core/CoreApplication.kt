package com.miageia2.threefortengame.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.socket.config.annotation.EnableWebSocket

//import org.springframework.context.annotation.Bean
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
@EnableFeignClients
@EnableWebSocket
class CoreApplication {

//	@Bean
//	fun passwordEncoder(): PasswordEncoder {
//		return BCryptPasswordEncoder() // Encodage sécurisé des mots de passe
//	}
}

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}
