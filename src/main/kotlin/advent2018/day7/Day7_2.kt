package advent2018.day7

import java.io.File

data class Candidate (val letter: Char, val predecessors: List<Candidate> = listOf()) {
    private val score = letter.toInt() - 4
    private fun superScore(): Int {
        return if (letter == '-') {
             0
        }
        else {
            score + predecessors.fold(0) { running: Int, current: Candidate ->
                Math.max(running, current.superScore())
            }
        }
    }

    override fun toString() = "Letter $letter predecessors ${predecessors.map {it.letter}} score ${superScore()}"
}

val NON_CANDIDATE = Candidate('-')

fun loadData() = File("src/main/resources/day7/input.txt")
    .readLines()
    .fold(
        Triple(
            mutableMapOf<Char, MutableList<Char>>(),
            mutableMapOf<Char, MutableList<Char>>(),
            mutableSetOf<Char>()
        )
    ) { (antecedents, subsequents, candidates), line ->
        lineRegex.matchEntire(line) ?. let { match ->
            val antecedent = match.groupValues[1][0]
            val subsequent = match.groupValues[2][0]
            candidates.add(antecedent)
            candidates.add(subsequent)

            antecedents.getOrPut(subsequent) { mutableListOf() } .add(antecedent)
            subsequents.getOrPut(antecedent) { mutableListOf() } .add(subsequent)
        }

        Triple(antecedents, subsequents, candidates)
    }

val threads = listOf(
    mutableListOf<Candidate>(),
    mutableListOf<Candidate>(),
    mutableListOf<Candidate>(),
    mutableListOf<Candidate>(),
    mutableListOf<Candidate>()
)

fun main(args: Array<String>) {
    val (antecedents, subsequents, candidates) = loadData()
    val currentWave = mutableListOf<Candidate>()
    currentWave.addAll(candidates.filter { !antecedents.contains(it) }.map { Candidate(it) })
    println("First wave $currentWave")
    val inPlay = mutableSetOf<Char>()
    var iteration = 0
    while (currentWave.isNotEmpty()) {
        inPlay.addAll(currentWave.map {it.letter})
        val nextWave = mutableListOf<Candidate>()

        threads.forEachIndexed { x, thread ->  thread.add(iteration, if (currentWave.size > x) currentWave[x] else NON_CANDIDATE) }

        for (candidate in currentWave) {
            subsequents[candidate.letter] ?. let {
                nextWave.addAll(it
                    .filter {
                        inPlay.containsAll(antecedents[it] ?: mutableListOf())
                    }
                    .map {Candidate(it, listOf(candidate))}
                )
            }
        }
        currentWave.clear()
        val mergedNext = nextWave.groupBy { it.letter } .map { entry -> Candidate(entry.key, entry.value.flatMap{it.predecessors})}
        currentWave.addAll(mergedNext)
        iteration++
    }
    println(threads[0].last())
}
