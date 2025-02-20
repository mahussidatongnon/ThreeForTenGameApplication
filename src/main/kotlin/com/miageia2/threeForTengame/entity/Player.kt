package com.miageia2.threeForTengame.entity

import lombok.Builder
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Builder
@Data
@Document(collection = "players")
class Player {
    @Id
    private val id: String? = null

    @Indexed(unique = true)
    private val username: String? = null // Unique dans la base de données

    private val gamesPlayed = 0
    private val gamesWon = 0
}