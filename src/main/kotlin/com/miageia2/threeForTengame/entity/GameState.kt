package com.miageia2.threeForTengame.entity

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


data class Move (
    var x: Int = 0,
    var y: Int = 0,
    var playerId: String? = null
)
@Data
@Document(collection = "game_states")
class GameState (
    @Id
    var id: String? = null,
    var gamePartId: String? = null, // Référence vers la partie
    var turn: Int = 0, // Numéro du tour
    var currentPlayerId: String? = null, // Joueur dont c'est le tour
    var boardState: Array<Array<String>>? = null, // Plateau sous forme de tableau 2D
    var lastMove: Move? = null, // Dernier mouvement
    var varidMoves: List<Move>? = null, // Liste des coups varides
    var isFinished: Boolean = false, // Indique si la partie est terminée
    var winnerId: String? = null, // Joueur gagnant (null si partie en cours)
    var timestamp: Instant? = null // Date/heure de l'état
)

