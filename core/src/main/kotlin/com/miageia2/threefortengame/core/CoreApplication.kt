package com.miageia2.threefortengame.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

//import org.springframework.context.annotation.Bean
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
@EnableFeignClients
class CoreApplication {

//	@Bean
//	fun passwordEncoder(): PasswordEncoder {
//		return BCryptPasswordEncoder() // Encodage sécurisé des mots de passe
//	}
}

fun main(args: Array<String>) {
	runApplication<CoreApplication>(*args)
}
