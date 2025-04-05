package com.miageia2.threefortengame.core.entity
import com.miageia2.threefortengame.core.entity.utils.BoardCell
import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant


data class Point (
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
    var turn: Int = 1, // Numéro du tour
    var currentPlayerIndex: Int = 0, // Joueur dont c'est le tour, 0 == joueur1, 1 == joueur2
    var currentPlayerId: String? = null,
    var boardState: Array<Array<BoardCell?>>? = null, // Plateau sous forme de tableau 2D
    var lastMove: PlayerTurn? = null, // Dernier mouvement
    var isFinished: Boolean = false, // Indique si la partie est terminée
    var createdAt: Instant? = null, // Date/heure de l'état
    var updatedAt: Instant? = null,
    var scores: HashMap<Int, Int> = hashMapOf(),
)

