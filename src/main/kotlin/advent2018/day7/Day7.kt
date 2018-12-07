package advent2018.day7

import java.io.File

val lineRegex = Regex("Step ([A-Z]) must be finished before step ([A-Z]) can begin\\.")

fun main(args: Array<String>) {
    val (antecedents, subsequents, candidates) = File("src/main/resources/day7/testInput.txt")
        .readLines()
        .fold(Triple(mutableMapOf<Char,MutableList<Char>>(), mutableMapOf<Char,MutableList<Char>>(), mutableSetOf<Char>())) { (antecedents, subsequents, candidates), line ->
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
    val candidatesWithNoAntecedents = candidates.filter { !antecedents.contains(it)}
    println("No antecedents $candidatesWithNoAntecedents")
}
