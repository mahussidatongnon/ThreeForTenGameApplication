package com.miageia2.threeForTengame.service

import com.miageia2.threeForTengame.dto.PointDTO
import com.miageia2.threeForTengame.entity.*
import com.miageia2.threeForTengame.entity.utils.BoardCell
import com.miageia2.threeForTengame.entity.utils.WinningDirection
import com.miageia2.threeForTengame.repository.GamePartRepository
import com.miageia2.threeForTengame.repository.GameStateRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.EnumMap

@Service
class GameStateService(
    private val gameStateRepository: GameStateRepository,
    private val gamePartRepository: GamePartRepository
) {


    fun findById(gameStateId: String): GameState {
        return gameStateRepository.findById(gameStateId).
        orElseThrow { IllegalArgumentException("GameState not found with id: $gameStateId") }!!
    }

    fun isInsideBoard(point: PointDTO, gameState: GameState): Boolean {
        val size = gameState.boardState!!.size
        return point.x in 0 until size && point.y in 0 until size
    }


    fun play(gamePart: GamePart, player: Player, coordinates: PointDTO, coinValue: Int): GameState {
        val gameState: GameState = gameStateRepository.findById(gamePart.gameStateId!!)
            .orElseThrow { IllegalArgumentException("GameState not found") }!!

        assert(gameState.boardState!![coordinates.x][coordinates.y] == null) { "Coordinate not valid" }

        // Placer le chiffre joué sur le plateau
        gameState.boardState!![coordinates.x][coordinates.y] = BoardCell(coinValue)

        // Vérifier les groupes de 3 cases totalisant 10
        val (pointsGagnes, wonPoints) = countWinningGroups(gameState, coordinates, player)


        gameState.apply {
            turn ++
            currentPlayerId = if(player.id == gamePart.player1Id) gamePart.player2Id else gamePart.player1Id
            updatedAt = Instant.now()
            lastMove = PlayerTurn(
                point = Point(coordinates.x, coordinates.y, player.id),
                value = coinValue,
                wonPoints = wonPoints
            )
        }

        // Mise à jour du score du joueur en s'assurant qu'il existe déjà dans la HashMap
        gamePart.scores[player.id!!] = gamePart.scores.getOrDefault(player.id, 0) + pointsGagnes
        gamePartRepository.save(gamePart)

        return gameStateRepository.save(gameState)
    }

    fun countWinningGroups(gameState: GameState, coordinates: PointDTO, player: Player): Pair<Int,
            EnumMap<WinningDirection, MutableSet<Point>>> {
        val directions = listOf(
            Pair(1, 0) to WinningDirection.HORIZONTAL,
            Pair(0, 1) to WinningDirection.VERTICAL,
            Pair(1, 1) to WinningDirection.DOWN_DIAGONAL,
            Pair(1, -1) to WinningDirection.UP_DIAGONAL
        )

        var totalPoints = 0
        val wonPoints: EnumMap<WinningDirection, MutableSet<Point>> = EnumMap<WinningDirection, MutableSet<Point>>(
            mutableMapOf<WinningDirection, MutableSet<Point>>().apply {
                WinningDirection.entries.forEach { this[it] = mutableSetOf() }
            }
        )

        for ((directionPair, direction) in directions) {
            val (dx, dy) = directionPair

            // Vérifie toutes les configurations possibles
            if (isWinningGroup(gameState, coordinates, dx, dy, player, direction)) {
                totalPoints++
                gameState.boardState!![coordinates.x][coordinates.y]?.wonCasesDirections?.add(direction)
                wonPoints[direction]?.addAll(
                    setOf(
                        Point(coordinates.x, coordinates.y, player.id),
                        Point(coordinates.x + dx, coordinates.y + dy, player.id),
                        Point(coordinates.x + 2 * dx, coordinates.y + 2 *  dy, player.id)
                    )
                )
            }
            if (isWinningGroup(gameState, coordinates, -dx, -dy, player, direction)) {
                totalPoints++
                gameState.boardState!![coordinates.x][coordinates.y]?.wonCasesDirections?.add(direction)
                wonPoints[direction]?.addAll(
                    setOf(
                        Point(coordinates.x, coordinates.y, player.id),
                        Point(coordinates.x - dx, coordinates.y - dy, player.id),
                        Point(coordinates.x - 2 * dx, coordinates.y - 2 *  dy, player.id)
                    )
                )
            }
            if (isWinningGroupMiddle(gameState, coordinates, dx, dy, player, direction)) {
                totalPoints++
                gameState.boardState!![coordinates.x][coordinates.y]?.wonCasesDirections?.add(direction)
                wonPoints[direction]?.addAll(
                    setOf(
                            Point(coordinates.x, coordinates.y, player.id),
                            Point(coordinates.x - dx, coordinates.y - dy, player.id),
                            Point(coordinates.x + dx, coordinates.y + dy, player.id)
                    )
                )
            }
        }

        return totalPoints to wonPoints
    }

    fun isWinningGroup(
        gameState: GameState,
        coordinates: PointDTO,
        dx: Int,
        dy: Int,
        player: Player,
        direction: WinningDirection
    ): Boolean {
        val board = gameState.boardState!!

        val x = coordinates.x
        val y = coordinates.y

        // Vérifie si cette case a déjà été utilisée dans cette direction
        val currentCell = board[x][y] ?: return false
        if (direction in currentCell.wonCasesDirections) {
            return false // On ne peut pas réutiliser cette case dans la même direction
        }

        val point1 = PointDTO(x + dx, y + dy)
        val point2 = PointDTO(x + 2 * dx, y + 2 * dy)

        if (!isInsideBoard(point1, gameState) || !isInsideBoard(point2, gameState)) {
            return false
        }

        val cell1 = board[point1.x][point1.y] ?: return false
        val cell2 = board[point2.x][point2.y] ?: return false

        return (currentCell.value + cell1.value + cell2.value == 10)
    }

    fun isWinningGroupMiddle(
        gameState: GameState,
        coordinates: PointDTO,
        dx: Int,
        dy: Int,
        player: Player,
        direction: WinningDirection
    ): Boolean {
        val board = gameState.boardState!!

        val x = coordinates.x
        val y = coordinates.y

        val currentCell = board[x][y] ?: return false
        if (direction in currentCell.wonCasesDirections) {
            return false // Déjà utilisée dans cette direction
        }

        val point1 = PointDTO(x - dx, y - dy)
        val point2 = PointDTO(x + dx, y + dy)

        if (!isInsideBoard(point1, gameState) || !isInsideBoard(point2, gameState)) {
            return false
        }

        val cell1 = board[point1.x][point1.y] ?: return false
        val cell2 = board[point2.x][point2.y] ?: return false

        return (currentCell.value + cell1.value + cell2.value == 10)
    }


}