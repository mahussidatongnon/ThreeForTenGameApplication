package com.miageia2.threefortengame.aiplayer.service.agent

import com.miageia2.threefortengame.common.dto.core.BoardCellDTO
import com.miageia2.threefortengame.common.dto.core.PointDTO
import com.miageia2.threefortengame.common.utils.core.WinningDirection
import java.util.*
import kotlin.random.Random

object MDP {
    fun getReward(state: State, action: Action): Double {
        val (pointsGagnes, wonPoints) = countWinningGroups(state, action.coordinates, action.coinValue)
        if(pointsGagnes == 0) return -0.6
        else return pointsGagnes.toDouble()
    }


    fun flipCoin(p: Double): Boolean {
        val r = Random.nextDouble()
//        println("flipCoin: p=$p, r=f$r")
        return r < p
    }

    fun deepCopy(state: State): State {
        return Array(state.size) { i ->
            Array(state[i].size) { j ->
                state[i][j]?.copy() // Utilise la méthode `copy()` si `BoardCellDTO` est un data class
            }
        }
    }

    fun simulateNextState(state: State, action: Action): State {
        val newState = deepCopy(state)
        val (x, y) = action.coordinates
        newState[x][y] = BoardCellDTO(value = action.coinValue)
        return newState
    }

    fun isTerminal(state: State): Boolean {
        return getLegalActions(state).isEmpty()
    }

    fun countWinningGroups(state: State, coordinates: PointDTO, coinValue: Int):
            Pair<Int, EnumMap<WinningDirection, MutableSet<PointDTO>>> {
        val directions = listOf(
            Pair(1, 0) to WinningDirection.HORIZONTAL,
            Pair(0, 1) to WinningDirection.VERTICAL,
            Pair(1, 1) to WinningDirection.DOWN_DIAGONAL,
            Pair(1, -1) to WinningDirection.UP_DIAGONAL
        )

        var totalPoints = 0
        val wonPoints: EnumMap<WinningDirection, MutableSet<PointDTO>> =
            EnumMap<WinningDirection, MutableSet<PointDTO>>(
                mutableMapOf<WinningDirection, MutableSet<PointDTO>>().apply {
                    WinningDirection.entries.forEach { this[it] = mutableSetOf() }
                }
            )

        for ((directionPair, direction) in directions) {
            val (dx, dy) = directionPair

            // Vérifie toutes les configurations possibles
            if (isWinningGroup(state, coordinates, coinValue, dx, dy, direction)) {
                totalPoints++
                wonPoints[direction]?.addAll(
                    setOf(
                        PointDTO(coordinates.x, coordinates.y),
                        PointDTO(coordinates.x + dx, coordinates.y + dy),
                        PointDTO(coordinates.x + (2 * dx), coordinates.y + (2 * dy))
                    )
                )
            }
            if (isWinningGroup(state, coordinates, coinValue, -dx, -dy, direction)) {
                totalPoints++
                wonPoints[direction]?.addAll(
                    setOf(
                        PointDTO(coordinates.x, coordinates.y),
                        PointDTO(coordinates.x - dx, coordinates.y - dy),
                        PointDTO(coordinates.x - (2 * dx), coordinates.y - (2 * dy))
                    )
                )
            }
            if (isWinningGroupMiddle(state, coordinates, coinValue, dx, dy, direction)) {
                totalPoints++
                wonPoints[direction]?.addAll(
                    setOf(
                        PointDTO(coordinates.x, coordinates.y),
                        PointDTO(coordinates.x - dx, coordinates.y - dy),
                        PointDTO(coordinates.x + dx, coordinates.y + dy)
                    )
                )
            }
        }

        return totalPoints to wonPoints
    }

    fun getLegalActions(state: State): List<Action> {
        val legalActions: MutableList<Action> = ArrayList()
        state.forEachIndexed { x, rowBoardCells ->
            rowBoardCells.withIndex()
                .filter { it.value == null }
                .forEach { item ->
                    for (value in 3..8) {
                        legalActions.add(
                            Action(
                                coordinates = PointDTO(x, item.index),
                                coinValue = value
                            )
                        )
                    }
                }
        }
        return legalActions.toList()
    }


    fun isWinningGroup(
        state: State,
        coordinates: PointDTO,
        coinValue: Int,
        dx: Int,
        dy: Int,
        direction: WinningDirection
    ): Boolean {

        val x = coordinates.x
        val y = coordinates.y

        val point1 = PointDTO(x + dx, y + dy)
        val point2 = PointDTO(x + (2 * dx), y + (2 * dy))

        if (!isInsideBoard(point1, state) || !isInsideBoard(point2, state)) {
            return false
        }

        val cell1 = state[point1.x][point1.y] ?: return false
        val cell2 = state[point2.x][point2.y] ?: return false

        // Vérifie si cette case a déjà été utilisée dans cette direction ar les 2 autres cellules
        if ((direction in cell1.wonCasesDirections) || (direction in cell2.wonCasesDirections)) {
            return false // On ne peut pas réutiliser cette case dans la même direction
        }

        return (coinValue + cell1.value + cell2.value) == 10
    }

    fun isWinningGroupMiddle(
        state: State,
        coordinates: PointDTO,
        coinValue: Int,
        dx: Int,
        dy: Int,
        direction: WinningDirection
    ): Boolean {
        val x = coordinates.x
        val y = coordinates.y

        val point1 = PointDTO(x - dx, y - dy)
        val point2 = PointDTO(x + dx, y + dy)

        if (!isInsideBoard(point1, state) || !isInsideBoard(point2, state)) {
            return false
        }

        val cell1 = state[point1.x][point1.y] ?: return false
        val cell2 = state[point2.x][point2.y] ?: return false

        // Vérifie si cette case a déjà été utilisée dans cette direction ar les 2 autres cellules
        if ((direction in cell1.wonCasesDirections) || (direction in cell2.wonCasesDirections)) {
            return false // On ne peut pas réutiliser cette case dans la même direction
        }

        return (coinValue + cell1.value + cell2.value == 10)
    }


    fun isInsideBoard(point: PointDTO, state: State): Boolean {
        val size = state.size
        return point.x in 0 until size && point.y in 0 until size
    }
}