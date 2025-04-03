package com.miageia2.threefortengame.core.service

import com.miageia2.threefortengame.common.dto.core.PointDTO
import com.miageia2.threefortengame.core.entity.*
import com.miageia2.threefortengame.core.entity.utils.BoardCell
import com.miageia2.threefortengame.common.utils.core.GamePartStatus
import com.miageia2.threefortengame.common.utils.core.WinningDirection
import com.miageia2.threefortengame.core.repository.GamePartRepository
import com.miageia2.threefortengame.core.repository.GameStateRepository
import com.miageia2.threefortengame.core.repository.PlayerTurnRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.EnumMap
import kotlin.math.pow

@Service
class GameStateService(
    private val gameStateRepository: GameStateRepository,
    private val gamePartRepository: GamePartRepository,
    private val playerTurnRepository: PlayerTurnRepository
) {


    fun findById(gameStateId: String): GameState {
        return gameStateRepository.findById(gameStateId).
        orElseThrow { IllegalArgumentException("GameState not found with id: $gameStateId") }!!
    }

    fun isInsideBoard(point: PointDTO, boardState: Array<Array<BoardCell?>>): Boolean {
        val size = boardState.size
        return point.x in 0 until size && point.y in 0 until size
    }


    fun play(gamePart: GamePart, player: Player, coordinates: PointDTO, coinValue: Int): GameState {
        val gameState: GameState = gameStateRepository.findById(gamePart.gameStateId!!)
            .orElseThrow { IllegalArgumentException("GameState not found") }!!

        if (!(coinValue in 3 until 9))
            throw IllegalArgumentException("CoinValue must be between 3 and 8")

        if (!isInsideBoard(coordinates, gameState.boardState!!))
            throw IllegalArgumentException("coordinates must be between 0 and ${gameState.boardState!!.size - 1}")

        if(player.id!=gameState.currentPlayerId)
            throw IllegalArgumentException("This is not your turn to play")

        // Vérifier si cette cas est toujours vide
        if (gameState.boardState!![coordinates.x][coordinates.y] != null)
            throw IllegalArgumentException("This case is already played")

        // Vérifier les groupes de 3 cases totalisant 10
        val (pointsGagnes, wonPoints) = countWinningGroups(gameState, coordinates, coinValue, player)


        gameState.apply {
            currentPlayerId = if(player.id == gamePart.player1Id) gamePart.player2Id else gamePart.player1Id
            updatedAt = Instant.now()
            lastMove = com.miageia2.threefortengame.core.entity.PlayerTurn(
                turn,
                point = Point(coordinates.x, coordinates.y, player.id),
                coinValue = coinValue,
                score = pointsGagnes,
                wonPoints = wonPoints,
                gameStateId = id!!,
            )
            turn ++
            isFinished = if (turn.compareTo(gamePart.nbCasesCote.toFloat().pow(2)) == 0) true else false

            // Mise à jour du score du joueur en s'assurant qu'il existe déjà dans la HashMap
            scores[player.id!!] = scores.getOrDefault(player.id, 0) + pointsGagnes

            winnerId = if(isFinished) scores.maxByOrNull { it.value }?.key else null

            boardState!![coordinates.x][coordinates.y] = BoardCell(
                value = coinValue,
                wonCasesDirections = wonPoints.filterValues { it.isNotEmpty() }.keys.toMutableSet()
            )

//          Mets à jours les points gagnants
            wonPoints.forEach {(direction, points) ->
                run {
                    points.forEach {
                        boardState!![it.x][it.y]?.wonCasesDirections?.add(direction)
                    }
                }
            }
        }

        if (gameState.isFinished)
            gamePart.status = GamePartStatus.FINISHED

        gamePartRepository.save(gamePart)
        playerTurnRepository.save(gameState.lastMove!!)

        return gameStateRepository.save(gameState)
    }

    private fun countWinningGroups(gameState: GameState, coordinates: PointDTO, coinValue: Int, player: Player):
            Pair<Int, EnumMap<WinningDirection, MutableSet<Point>>> {
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
            if (isWinningGroup(gameState, coordinates, coinValue, dx, dy, direction)) {
                totalPoints++
                wonPoints[direction]?.addAll(
                    setOf(
                        Point(coordinates.x, coordinates.y, player.id),
                        Point(coordinates.x + dx, coordinates.y + dy, player.id),
                        Point(coordinates.x + (2 * dx), coordinates.y + (2 *  dy), player.id)
                    )
                )
            }
            if (isWinningGroup(gameState, coordinates, coinValue, -dx, -dy, direction)) {
                totalPoints++
                wonPoints[direction]?.addAll(
                    setOf(
                        Point(coordinates.x, coordinates.y, player.id),
                        Point(coordinates.x - dx, coordinates.y - dy, player.id),
                        Point(coordinates.x - (2 * dx), coordinates.y - (2 *  dy), player.id)
                    )
                )
            }
            if (isWinningGroupMiddle(gameState, coordinates, coinValue, dx, dy, direction)) {
                totalPoints++
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

    private fun isWinningGroup(
        gameState: GameState,
        coordinates: PointDTO,
        coinValue: Int,
        dx: Int,
        dy: Int,
        direction: WinningDirection
    ): Boolean {
        val board = gameState.boardState!!

        val x = coordinates.x
        val y = coordinates.y

//        println("x=$x, y=$y")

        val point1 = PointDTO(x + dx, y + dy)
        val point2 = PointDTO(x + (2 * dx), y + (2 * dy))
//        println("point1=$point1, point2=$point2")

        if (!isInsideBoard(point1, gameState.boardState!!) || !isInsideBoard(point2, gameState.boardState!!)) {
            return false
        }

//        println("Is inside board")
//        println("cell1=${board[point1.x][point1.y]}, cell2=${board[point2.x][point2.y]}")

        val cell1 = board[point1.x][point1.y] ?: return false
        val cell2 = board[point2.x][point2.y] ?: return false

//        println("cell1=$cell1, cell2=$cell2")

        // Vérifie si cette case a déjà été utilisée dans cette direction ar les 2 autres cellules
        if ((direction in cell1.wonCasesDirections) || (direction in cell2.wonCasesDirections)) {
            return false // On ne peut pas réutiliser cette case dans la même direction
        }

//        println("Somme: ${(coinValue + cell1.value + cell2.value)}")
        return (coinValue + cell1.value + cell2.value) == 10
    }

    private fun isWinningGroupMiddle(
        gameState: GameState,
        coordinates: PointDTO,
        coinValue: Int,
        dx: Int,
        dy: Int,
        direction: WinningDirection
    ): Boolean {
        val board = gameState.boardState!!

        val x = coordinates.x
        val y = coordinates.y

        val point1 = PointDTO(x - dx, y - dy)
        val point2 = PointDTO(x + dx, y + dy)

        if (!isInsideBoard(point1, gameState.boardState!!) || !isInsideBoard(point2, gameState.boardState!!)) {
            return false
        }

        val cell1 = board[point1.x][point1.y] ?: return false
        val cell2 = board[point2.x][point2.y] ?: return false

        // Vérifie si cette case a déjà été utilisée dans cette direction ar les 2 autres cellules
        if ((direction in cell1.wonCasesDirections) || (direction in cell2.wonCasesDirections)) {
            return false // On ne peut pas réutiliser cette case dans la même direction
        }

        return (coinValue + cell1.value + cell2.value == 10)
    }

    fun getLegalAction(gameStateId: String): Array<PointDTO> {
        val gameState: GameState = gameStateRepository.findById(gameStateId)
            .orElseThrow { IllegalArgumentException("GameState not found") }!!

        val legalActions: MutableList<PointDTO> = ArrayList()
        println(gameState.id)
        println(gameState.boardState!![0][0])
        gameState.boardState!!.forEachIndexed { x, rowBoardCells ->
            rowBoardCells.withIndex()
                .filter { it.value == null }
                .forEach { item ->
                    legalActions.add(PointDTO(x, item.index))
                }
        }
        return legalActions.toTypedArray()
    }
}