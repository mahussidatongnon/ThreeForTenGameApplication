package com.miageia2.threefortengame.aiplayer.service.agent

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.*

//data class QEntry(val state: String, val action: Action, val qValue: Double)
data class QEntry(val state: String, val action: Action, val qvalue: Double)


class QLearningAgent(override val startState: State, override val alpha: Double = 1.0, override val epsilon: Double = 0.5, gamma: Double = 0.8): Agent(startState, alpha, epsilon, gamma) {
    private var _qValues: HashMap<Pair<State, Action>, Double> = HashMap()
    val qValues = _qValues
    private var _nbExplore = 0
    val nbExplore: Int
        get() = _nbExplore

    override fun getQValue(state: State, action: Action): Double {
//        println("Key: ${Pair(state, action)}")
//        println("is qvalue exist?: ${_qValues[Pair(state, action)]}")
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
        if (legalActions.isEmpty()) {
            println("LegalAction is empty")
            return null
        }
        val maxQ = legalActions.maxOf { action -> getQValue(state, action) }
        val bestActions = legalActions.filter { action -> getQValue(state, action) == maxQ }
        return bestActions.randomOrNull()
    }

    fun liveGetQValue(state: State, action: Action): Double {
//        println("Key: ${Pair(state, action)}")
//        println("is qvalue exist?: ${_qValues[Pair(state, action)]}")
//        return _qValues[Pair(state, action)] ?: 0.0
        val qValue = _qValues[Pair(state, action)] ?: 0.0
        val reward = this.mdp.getReward(state, action)
        return qValue + reward
    }

    fun liveComputeActionFromQValues(state: State): Action? {
        val legalActions = mdp.getLegalActions(state)
        if (legalActions.isEmpty()) {
            println("LegalAction is empty")
            return null
        }

        val maxQ = legalActions.maxOf { action -> liveGetQValue(state, action) }
        val bestActions = legalActions.filter { action -> liveGetQValue(state, action) == maxQ }
        return bestActions.randomOrNull()
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
        return liveComputeActionFromQValues(state)
//        return computeActionFromQValues(state)
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

//        val qEntries = qValues.map { (key, value) ->
//            val (state, action) = key
//            val stateJson = mapper.writeValueAsString(state.board) // Encodage de l'état
//            QEntry(stateJson, action, value)
//        }

        val file = File(filePath)
        val parentDir = file.parentFile

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs() // Crée tous les dossiers parents si nécessaire
        }

        BufferedWriter(FileWriter(file)).use { writer ->
            for ((key, value) in qValues) {
                val (state, action) = key
                val stateJson = mapper.writeValueAsString(state.board) // Encode seulement le plateau
                val entry = QEntry(stateJson, action, value)
                val line = mapper.writeValueAsString(entry)
                writer.write(line)
                writer.newLine() // Une entrée JSON par ligne
            }
        }
    }

    fun loadQValues(qValues: HashMap<Pair<State, Action>, Double>?) {
        if (qValues != null) {
            _qValues = qValues
        } else {
            _qValues = HashMap<Pair<State, Action>, Double>()
        }
    }
    fun loadQValues(filePath: String): HashMap<Pair<State, Action>, Double> {
        val mapper = jacksonObjectMapper()

        val qValues = HashMap<Pair<State, Action>, Double>()

        try {
            BufferedReader(FileReader(filePath)).use { reader ->
                reader.lineSequence().forEach { line ->
                    val entry = mapper.readValue(line, QEntry::class.java)
                    val state = State(mapper.readValue(entry.state))
                    qValues[Pair(state, entry.action)] = entry.qvalue
                }
            }
        } catch (e: Exception) {
            println("Load QValues error: ${e.message}")
        }

        return qValues
    }

}