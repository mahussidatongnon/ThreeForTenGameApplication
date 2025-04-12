package com.miageia2.threefortengame.aiplayer.service.agent

import com.miageia2.threefortengame.common.dto.core.BoardCellDTO
import com.miageia2.threefortengame.common.dto.core.PointDTO
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Instant


typealias State = Array<Array<BoardCellDTO?>>

data class Action(
    val coordinates: PointDTO,
    val coinValue: Int,
)

/**
 *       Abstract agent which assigns values to (state,action)
 *       Q-Values for an environment. As well as a value to a
 *       state and a policy given respectively by,
 *
 *       V(s) = max_{a in actions} Q(s,a)
 *       policy(s) = arg_max_{a in actions} Q(s,a)
 *
 *       Both ValueIterationAgent and QLearningAgent inherit
 *       from this agent. While a ValueIterationAgent has
 *       a model of the environment via a MarkovDecisionProcess
 *       (see mdp.py) that is used to estimate Q-Values before
 *       ever actually acting, the QLearningAgent estimates
 *       Q-Values while acting in the environment.
 */
open class Agent(open val startState: State, open val alpha: Double = 1.0, open val epsilon: Double = 0.5, gamma: Double = 0.8) {

    protected val logger = LoggerFactory.getLogger(javaClass)

    protected var _episodeRewards: Double = 0.0
    protected var _lastState: State? = null
    protected var _lastAction: Action? = null
    protected var _score: Double = 0.0
    protected var _discount: Double = gamma
    protected var state: State = startState
    protected val mdp: MDP = MDP


    /**
     * The Agent will receive a GameState and
     * must return an action from Directions
     */
    open fun getAction(state: State): Action? {
        throw NotImplementedError()
    }

    /**
     * Should return q_value
     */
    open fun getQValue(state: State, action: Action): Double {
        throw NotImplementedError()
    }

    /**
     * What is the value of this state under the best action?
     *         Concretely, this is given by
     *         V(s) = max_{a in actions} Q(s,a)
     */
    open fun getValue(state: State): Double {
        throw NotImplementedError()
    }

    /**
     * What is the best action to take in the state. Note that because
     *         we might want to explore, this might not coincide with getAction
     *         Concretely, this is given by
     *
     *         policy(s) = arg_max_{a in actions} Q(s,a)
     *
     *         If many actions achieve the maximal Q-value,
     *         it doesn't matter which is selected.
     */
    open fun getPolicy(state: State): Action? {
        throw NotImplementedError()
    }

    /**
     * This class will call this function, which you write, after
     *         observing a transition and reward
     */
    open fun update(state: State, action: Action, nextState: State, reward: Double) {
        throw NotImplementedError()
    }

    fun observeTransition(state: State, action: Action, nextState: State, deltaReward: Double) {
        this._episodeRewards += deltaReward
        this.update(state, action, nextState, deltaReward)
    }

    open fun stopEpisode() {
        throw NotImplementedError()
    }

    /**
     *     Called by inherited class when
     *     an action is taken in a state
     */
    fun doAction(state: State, action: Action) {
        _lastAction = action
        _lastAction = action
        val (pointsGagnes, _) = mdp.countWinningGroups(state, action.coordinates, action.coinValue)
        _score += pointsGagnes
    }


    companion object {
        fun train(agent: Agent, numEpisodes: Int) {
            var totalScore = 0.0
            repeat(numEpisodes) { episode ->
                var currentState = agent.mdp.deepCopy(agent.startState)
                var done = false

                while (!done) {
                    val action = agent.getAction(currentState) ?: break
                    val nextState = agent.mdp.simulateNextState(currentState, action)
                    val reward = agent.mdp.getReward(currentState, action)

                    agent.observeTransition(currentState, action, nextState, reward)
                    agent.doAction(currentState, action)

                    currentState = nextState
                    done = agent.mdp.isTerminal(currentState)
                }

                println("Episode ${episode + 1} terminé. Score = ${agent._score}")
                if (agent is QLearningAgent)
                    println("Nb Explorer ${agent.nbExplore}")
                totalScore += agent._score
                agent.stopEpisode()
            }
            val meanScore = totalScore / numEpisodes
            if (agent is QLearningAgent) {
                agent.saveQValues("qValues/${agent.startState.size}SquareCases/${numEpisodes}Episodes_MeanScore" +
                        "${meanScore}_Alpha${agent.alpha}_Epsilon${agent.epsilon}_Gamma${agent._discount}_Time${Instant.now()}.json", agent.qValues)
            }
            println("Mean score = $meanScore")
        }
    }
}