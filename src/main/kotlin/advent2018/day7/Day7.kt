package advent2018.day7

import java.io.File

val lineRegex = Regex("Step ([A-Z]) must be finished before step ([A-Z]) can begin\\.")

fun main(args: Array<String>) {
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
    val candidatesWithNoAntecedents = mutableListOf<Char>()
    candidatesWithNoAntecedents.addAll(candidates.filter { !antecedents.contains(it) })
    println("No antecedents $candidatesWithNoAntecedents")
    val result = mutableListOf<Char>()
    val inPlay = mutableSetOf<Char>()
    while (candidatesWithNoAntecedents.isNotEmpty() ) {
        inPlay.addAll(candidatesWithNoAntecedents)
        candidatesWithNoAntecedents.sort()
        val next = candidatesWithNoAntecedents.removeAt(0)
        result.add(next)
        val nextSubsequents = subsequents[next]
        if (nextSubsequents != null) {
            val newCandidates = nextSubsequents.filter {
                (!inPlay.contains(it)) &&
                result.containsAll(antecedents[it] ?: mutableListOf())
            }
            candidatesWithNoAntecedents.addAll(newCandidates)
        }
    }
    println("Result $result")
}
