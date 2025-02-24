package com.miageia2.threeForTengame.service

import com.miageia2.threeForTengame.dto.PointDTO
import com.miageia2.threeForTengame.entity.GamePart
import com.miageia2.threeForTengame.entity.GameState
import com.miageia2.threeForTengame.entity.Player
import com.miageia2.threeForTengame.entity.Point
import com.miageia2.threeForTengame.entity.utils.GamePointDetail
import com.miageia2.threeForTengame.entity.utils.WinningCase
import com.miageia2.threeForTengame.repository.GameStateRepository
import com.miageia2.threeForTengame.service.utils.WinnablePoint
import org.springframework.stereotype.Service

@Service
class GameStateService(val gameStateRepository: GameStateRepository) {

    fun play(gamePart: GamePart, player: Player, coordinates: PointDTO): GameState {
        val gameState = gameStateRepository.findById(gamePart.gameStateId!!).
            orElseThrow { IllegalArgumentException("GameState not found") }!!


        if (!isValidCoordinates(gameState.boardState!!, coordinates))
            throw IllegalArgumentException("Coordinate not valid")

        // check rules etc ...
        return gameState
    }

    fun findById(gameStateId: String): GameState {
        return gameStateRepository.findById(gameStateId).
        orElseThrow { IllegalArgumentException("GameState not found with id: $gameStateId") }!!
    }

    fun isValidCoordinates(boardState: Array<Array<GamePointDetail?>>, coordinates: PointDTO): Boolean {
        return boardState[coordinates.x][coordinates.y] != null
    }

    fun applyCoordinates(gameState: GameState, coordinates: PointDTO, player: Player) {
//      set a coin to coordinate by player
        assert( isValidCoordinates(gameState.boardState!!, coordinates)) { "Coordinate not valid" }
//        val winnablePoints: List<WinnablePoint> = this.getWinnablePoints(
//            gameState.boardState!!, coordinates, player)
    }

    fun isWinnableCase(point1: PointDTO, point2: PointDTO, winningCase: WinningCase, nbCasesCote: Int, player: Player,
                       gamePointDetail1: GamePointDetail?, gamePointDetail2: GamePointDetail?): Boolean {
        if (point2.x > nbCasesCote || point2.y > nbCasesCote)
            return false
//      winningCase is possible


        if (gamePointDetail1 == null || gamePointDetail2 == null)
            return false

//      upDiagonal point exists
        if (gamePointDetail1.playerId != player.id || gamePointDetail2.playerId != player.id)
            return false
//
//      The pointDetail belongs to players
        if ((winningCase in gamePointDetail1.winningCases) || (winningCase in gamePointDetail2.winningCases))
            return false

//      The winning is available for both points
        return true
    }

    fun getWinnablePoints(boardState: Array<Array<GamePointDetail?>>, coordinates: PointDTO, player: Player ) : List<WinnablePoint> {
        val nbCasesCote = boardState.size
        val winnablePoints: MutableList<WinnablePoint> = mutableListOf()
        // Chaque case peut être utilisée quatre fois au plus pour faire un point : horizontalement, verticalement,
        // en diagonale montante et en diagonale descendante. Elle ne peut pas être utilisée deux fois dans
        // la même direction !
//        var has_up_diagonal = false
//        var has_down_diagonal = false
//        var has_vertical = false
//        var has_horizontal = false
        if (isWinnableCase(
            point1 = PointDTO(coordinates.x + 1, coordinates.y + 1),
            point2 = PointDTO(coordinates.x + 2, coordinates.y + 2),
            winningCase = WinningCase.UP_RIGHT_DIAGONAL,
            nbCasesCote = nbCasesCote,
            player = player,
            gamePointDetail1 = boardState[coordinates.x + 1][coordinates.y + 1],
            gamePointDetail2 = boardState[coordinates.x + 2][coordinates.y + 2],
        ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x + 1, coordinates.y + 1, player.id),
                Point(coordinates.x + 2, coordinates.y + 2, player.id),
                WinningCase.UP_RIGHT_DIAGONAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x + 1, coordinates.y - 1),
                point2 = PointDTO(coordinates.x + 2, coordinates.y - 2),
                winningCase = WinningCase.DOWN_RIGHT_DIAGONAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x + 1][coordinates.y - 1],
                gamePointDetail2 = boardState[coordinates.x + 2][coordinates.y - 2],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x + 1, coordinates.y - 1, player.id),
                Point(coordinates.x + 2, coordinates.y - 2, player.id),
                WinningCase.UP_RIGHT_DIAGONAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x - 1, coordinates.y + 1),
                point2 = PointDTO(coordinates.x - 2, coordinates.y + 2),
                winningCase = WinningCase.UP_LEFT_DIAGONAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x - 1][coordinates.y + 1],
                gamePointDetail2 = boardState[coordinates.x - 2][coordinates.y + 2],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x - 1, coordinates.y + 1, player.id),
                Point(coordinates.x - 2, coordinates.y + 2, player.id),
                WinningCase.UP_LEFT_DIAGONAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x - 1, coordinates.y - 1),
                point2 = PointDTO(coordinates.x - 2, coordinates.y - 2),
                winningCase = WinningCase.DOWN_LEFT_DIAGONAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x - 1][coordinates.y - 1],
                gamePointDetail2 = boardState[coordinates.x - 2][coordinates.y - 2],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x - 1, coordinates.y - 1, player.id),
                Point(coordinates.x - 2, coordinates.y - 2, player.id),
                WinningCase.DOWN_LEFT_DIAGONAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x, coordinates.y + 1),
                point2 = PointDTO(coordinates.x, coordinates.y + 2),
                winningCase = WinningCase.UP_VERTICAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x][coordinates.y + 1],
                gamePointDetail2 = boardState[coordinates.x][coordinates.y + 2],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x, coordinates.y + 1, player.id),
                Point(coordinates.x, coordinates.y + 2, player.id),
                WinningCase.UP_VERTICAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x, coordinates.y - 1),
                point2 = PointDTO(coordinates.x, coordinates.y - 2),
                winningCase = WinningCase.DOWN_VERTICAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x][coordinates.y - 1],
                gamePointDetail2 = boardState[coordinates.x][coordinates.y - 2],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x, coordinates.y - 1, player.id),
                Point(coordinates.x, coordinates.y - 2, player.id),
                WinningCase.DOWN_VERTICAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x - 1, coordinates.y),
                point2 = PointDTO(coordinates.x - 2, coordinates.y),
                winningCase = WinningCase.LEFT_HORIZONTAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x - 1][coordinates.y],
                gamePointDetail2 = boardState[coordinates.x - 2][coordinates.y],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x - 1, coordinates.y, player.id),
                Point(coordinates.x - 2, coordinates.y, player.id),
                WinningCase.LEFT_HORIZONTAL,
            ))

        if (isWinnableCase(
                point1 = PointDTO(coordinates.x + 1, coordinates.y),
                point2 = PointDTO(coordinates.x + 2, coordinates.y),
                winningCase = WinningCase.RIGHT_HORIZONTAL,
                nbCasesCote = nbCasesCote,
                player = player,
                gamePointDetail1 = boardState[coordinates.x + 1][coordinates.y],
                gamePointDetail2 = boardState[coordinates.x + 2][coordinates.y],
            ))
            winnablePoints.add(WinnablePoint(
                Point(coordinates.x + 1, coordinates.y, player.id),
                Point(coordinates.x + 2, coordinates.y, player.id),
                WinningCase.RIGHT_HORIZONTAL,
            ))

        return winnablePoints.toList()
    }

}