package com.miageia2.threefortengame.common.dto.core

import java.time.Instant

class GameStateDTO (
    var id: String? = null,
    var gamePartId: String? = null, // Référence vers la partie
    var turn: Int = 1, // Numéro du tour
    var currentPlayerIndex: Int = 0, // Joueur dont c'est le tour
    var currentPlayerId: String? = null,
    var boardState: Array<Array<BoardCellDTO?>>? = null, // Plateau sous forme de tableau 2D
    var lastMove: PlayerTurnDTO? = null, // Dernier mouvement
    var isFinished: Boolean = false, // Indique si la partie est terminée
    var winnerIndex: Int? = null, // Joueur gagnant (null si partie en cours)
    var createdAt: Instant? = null, // Date/heure de l'état
    var updatedAt: Instant? = null,
    var scores: HashMap<Int, Int> = hashMapOf(),
)