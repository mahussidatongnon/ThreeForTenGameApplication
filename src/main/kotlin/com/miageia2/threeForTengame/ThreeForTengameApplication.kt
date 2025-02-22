package com.miageia2.threeForTengame

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
//import org.springframework.context.annotation.Bean
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class ThreeForTengameApplication {

//	@Bean
//	fun passwordEncoder(): PasswordEncoder {
//		return BCryptPasswordEncoder() // Encodage sécurisé des mots de passe
//	}
}

fun main(args: Array<String>) {
	runApplication<ThreeForTengameApplication>(*args)
}
