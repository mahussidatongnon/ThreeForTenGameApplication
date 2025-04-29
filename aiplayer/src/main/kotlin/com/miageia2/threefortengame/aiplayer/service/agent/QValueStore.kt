package com.miageia2.threefortengame.aiplayer.service.agent

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileReader
import java.io.BufferedReader

@Service
class QValueStore {

    private val mapper = jacksonObjectMapper()

    // Représente : Map<PlateauSize, Q-values>
    private val qValuesMap: MutableMap<Int, HashMap<Pair<State, Action>, Double>> = mutableMapOf()

//    private val supportedSizes = listOf(5, 6, 7)
    private val supportedSizes = listOf(5)
//    private val supportedSizes = emptyList<Int>()

    fun getQValues(size: Int): HashMap<Pair<State, Action>, Double>? {
        return qValuesMap[size]
    }

    @PostConstruct
    fun init() {
        supportedSizes.forEach { size ->
            val filePath = "app/qValues/${size}Best.jsonl"
            println("Begin loading of qValues for size $size")
            val qValues = loadQValues(filePath)
            if (qValues != null) {
                qValuesMap[size] = qValues
                println("✅ Q-values loaded for size $size (${qValues.size} entries)")
            } else {
                println("⚠️ Q-values for size $size could not be loaded.")
                qValuesMap[size] = HashMap()
            }
        }
    }

    private fun loadQValues(filePath: String): HashMap<Pair<State, Action>, Double>? {
        val qValues = HashMap<Pair<State, Action>, Double>()
        return try {
            BufferedReader(FileReader(File(filePath))).use { reader ->
                reader.lineSequence().forEach { line ->
                    val entry = mapper.readValue<QEntry>(line)
                    val state = State(mapper.readValue(entry.state))
                    qValues[Pair(state, entry.action)] = entry.qvalue
                }
            }
            qValues
        } catch (e: Exception) {
            println("❌ Error loading $filePath: ${e.message}")
            null
        }
    }
}
