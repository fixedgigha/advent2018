package advent2018.day7

import java.io.File

data class Candidate (val letter: Char, val immediatePredecessor: List<Candidate> = listOf()) {
    val score = letter.toInt() - 4
    fun superScore(): Int {
        if (letter == '-') {
            return 0
        }
        return score + immediatePredecessor.fold(0) { running: Int, current: Candidate -> Math.max(running, current.superScore())}
    }

    override fun toString() = "Letter $letter predecessors ${immediatePredecessor.map {it.letter}} score ${superScore()}"
}

val NON_CANDIDATE = Candidate('-')

fun main(args: Array<String>) {
    val lineRegex = Regex("Step ([A-Z]) must be finished before step ([A-Z]) can begin\\.")
    val (antecedents, subsequents, candidates) = File("src/main/resources/day7/input.txt")
        .readLines()
        .fold(
            Triple(
                mutableMapOf<Char, MutableList<Char>>(),
                mutableMapOf<Char, MutableList<Char>>(),
                mutableSetOf<Char>()
            )
        ) { (antecedents, subsequents, candidates), line ->
            val match = lineRegex.matchEntire(line)
            if (match != null) {
                val antecedent = match.groupValues[1][0]
                val subsequent = match.groupValues[2][0]
                candidates.add(antecedent)
                candidates.add(subsequent)

                antecedents[subsequent] = antecedents.getOrDefault(subsequent, mutableListOf())
                antecedents[subsequent]?.add(antecedent)
                subsequents[antecedent] = subsequents.getOrDefault(antecedent, mutableListOf())
                subsequents[antecedent]?.add(subsequent)
            }

            Triple(antecedents, subsequents, candidates)
        }
    var candidatesWithNoAntecedents = mutableListOf<Candidate>()
    candidatesWithNoAntecedents.addAll(candidates.filter { !antecedents.contains(it) }.map { Candidate(it) })
    println("No antecedents $candidatesWithNoAntecedents")
    val threads = listOf(
            mutableListOf<Candidate>(),
            mutableListOf<Candidate>(),
            mutableListOf<Candidate>(),
            mutableListOf<Candidate>(),
            mutableListOf<Candidate>()
    )
    val inPlay = mutableSetOf<Char>()
    var iteration = 0
    while (candidatesWithNoAntecedents.isNotEmpty() ) {
        inPlay.addAll(candidatesWithNoAntecedents.map {it.letter})
        val nextCandidatesWithNoAntecedents = mutableListOf<Candidate>()

        for (x in 0..4) {
            threads[x].add(iteration, if (candidatesWithNoAntecedents.size > x) candidatesWithNoAntecedents[x] else NON_CANDIDATE)
        }
        for (candidatesWithNoAntecedent in candidatesWithNoAntecedents) {
            val nextSubsequents = subsequents[candidatesWithNoAntecedent.letter]
            if (nextSubsequents != null) {
                val newCandidates = nextSubsequents.filter {
                    (!inPlay.contains(it)) && inPlay.containsAll(antecedents[it] ?: mutableListOf())
                }
                nextCandidatesWithNoAntecedents.addAll(newCandidates.map {Candidate(it, listOf(candidatesWithNoAntecedent))})
            }
        }
        candidatesWithNoAntecedents.clear()
        val mergedNext = nextCandidatesWithNoAntecedents.groupBy { it.letter } .map {entry -> Candidate(entry.key, entry.value.flatMap{it.immediatePredecessor})}
        candidatesWithNoAntecedents.addAll(mergedNext)
        iteration++
    }
    println(threads[0].last())
}
