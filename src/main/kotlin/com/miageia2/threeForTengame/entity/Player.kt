package com.miageia2.threeForTengame.entity

import lombok.Builder
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
    val username: String? = null, // Unique dans la base de données

    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0
)