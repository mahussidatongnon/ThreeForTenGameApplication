package com.miageia2.threefortengame.core.entity

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document(collection = "players")
data class Player  (
    @Id
    val id: String? = null,

    @Indexed(unique = true)
    val username: String, // Unique dans la base de données
    val password: String? = null,

    var gamesPlayed: Int = 0,
    var gamesWon: Int = 0
)