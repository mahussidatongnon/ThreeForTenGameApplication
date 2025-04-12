package com.miageia2.threefortengame.aiplayer.service.agent

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class QEntry(val state: String, val action: Action, val qValue: Double)

class QLearningAgent(override val startState: State, override val alpha: Double = 1.0, override val epsilon: Double = 0.5, gamma: Double = 0.8): Agent(startState, alpha, epsilon, gamma) {
    private var _qValues: HashMap<Pair<State, Action>, Double> = HashMap()
    val qValues = _qValues
    private var _nbExplore = 0
    val nbExplore: Int
        get() = _nbExplore

    override fun getQValue(state: State, action: Action): Double {
        return _qValues[Pair(state, action)] ?: 0.0
    }

    /**
     * Returns max_action Q(state,action)
     *           where the max is over legal actions.  Note that if
     *           there are no legal actions, which is the case at the
     *           terminal state, you should return a value of 0.0.
     */
    fun computeValueFromQValues(state: State): Double {
        val legalActions = mdp.getLegalActions(state)
        if (legalActions.isEmpty()) return 0.0
        return legalActions.maxOf { action -> getQValue(state, action)  }
    }

    /**
     * Compute the best action to take in a state.  Note that if there
     *           are no legal actions, which is the case at the terminal state,
     *           you should return None.
     */
    fun computeActionFromQValues(state: State): Action? {
        val legalActions = mdp.getLegalActions(state)
        if (legalActions.isEmpty()) return null
        return legalActions.maxBy { action -> getQValue(state, action) }
    }

    override fun getAction(state: State): Action? {
        val legalActions = mdp.getLegalActions(state)
        if (legalActions.isEmpty()) return null
        val explore = mdp.flipCoin(epsilon)
        if(!explore) {
//            println("Following best qValue")
            return computeActionFromQValues(state)
        }
        else {
            _nbExplore++
//            println("Exploring environnement")
            return legalActions.random()
        }
    }

    override fun getValue(state: State): Double {
        return computeValueFromQValues(state)
    }

    override fun getPolicy(state: State): Action? {
        return computeActionFromQValues(state)
    }

    override fun update(state: State, action: Action, nextState: State, reward: Double) {
        val expectedQValue = reward + _discount + computeValueFromQValues(nextState)
        val deltaQValue = expectedQValue - getQValue(state, action)
        val newQValue = getQValue(state, action) +  alpha * deltaQValue
        _qValues[Pair(state, action)] = newQValue
    }

    override fun stopEpisode() {
        _episodeRewards = 0.0
        _lastAction = null
        _lastState = null
        _score = 0.0
        _nbExplore = 0
    }

    fun saveQValues(filePath: String, qValues: HashMap<Pair<State, Action>, Double>) {
        val mapper = jacksonObjectMapper()

        val qEntries = qValues.map { (key, value) ->
            val (state, action) = key
            val stateJson = mapper.writeValueAsString(state) // Encodage de l'état
            QEntry(stateJson, action, value)
        }

        val file = File(filePath)
        val parentDir = file.parentFile

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs() // Crée tous les dossiers parents si nécessaire
        }

        mapper.writeValue(file, qEntries)
    }

    fun loadQValues(filePath: String): HashMap<Pair<State, Action>, Double> {
        val mapper = jacksonObjectMapper()
        val qEntries: List<QEntry> = mapper.readValue(File(filePath))

        val qValues = HashMap<Pair<State, Action>, Double>()

        for (entry in qEntries) {
            val state: State = mapper.readValue(entry.state) // re-décode le State
            qValues[Pair(state, entry.action)] = entry.qValue
        }

        return qValues
    }
}