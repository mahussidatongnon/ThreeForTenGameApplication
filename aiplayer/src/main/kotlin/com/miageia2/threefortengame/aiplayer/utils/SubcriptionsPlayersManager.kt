package com.miageia2.threefortengame.aiplayer.utils

import com.miageia2.threefortengame.common.AiPlayerType
import java.time.Instant

data class SubscriptionPlayerInfo(
    val playerId: String,
    val aiPlayerType: AiPlayerType,
    val lastPlayedAt: Instant? = null,
)
object SubscriptionsPlayersManager {

    private val subscriptionsPlayers: HashMap<String, MutableList<SubscriptionPlayerInfo>> = HashMap()

    fun addItem(gameID: String, subscriptionPlayerInfo: SubscriptionPlayerInfo) {
        if (subscriptionsPlayers.containsKey(gameID)) {
            val index = subscriptionsPlayers[gameID]!!.indexOfFirst { it.playerId == subscriptionPlayerInfo.playerId }
            if (index == -1) {
                subscriptionsPlayers[gameID]!!.add(subscriptionPlayerInfo)
            } else
                println("Ce joueur a déjà été enrégister")
        } else
            subscriptionsPlayers[gameID] = mutableListOf(subscriptionPlayerInfo)
        println("Item added: $subscriptionPlayerInfo")
    }

    fun updateLastPlayedAt(gameID: String, playerId: String) {
        if (subscriptionsPlayers.containsKey(gameID)){
            val index = subscriptionsPlayers[gameID]!!.indexOfFirst { it.playerId == playerId }
            if (index != -1)
                subscriptionsPlayers[gameID]!![index] = subscriptionsPlayers[gameID]!![index].copy(lastPlayedAt = Instant.now())
            else
                println("The player $playerId is no longer registered")
        }
        else
            println("gameID: $gameID does not exist")
    }

    fun getItem(gameID: String, playerId: String): SubscriptionPlayerInfo? {
        return subscriptionsPlayers[gameID]?.find { it.playerId == playerId }
    }

    fun containsGameID(gameID: String): Boolean {
        return subscriptionsPlayers.containsKey(gameID)
    }
}