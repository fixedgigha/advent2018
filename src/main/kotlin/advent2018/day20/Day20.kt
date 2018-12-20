package advent2018.day20

import java.io.File

val text = File("src/main/resources/day20/input.txt").readText()
var current = 1

fun score(): Int {
    val branchScores = mutableListOf(0)
    var currentBranch = 0
    var finished = false
    while (!finished) {
        print(text[current])
        when(text[current]) {
            '$', ')' -> {
                current++
                finished = true
             }
            '|' -> {
                current++
                currentBranch++
                branchScores.add(0)
            }
            '(' -> {
                current++
                branchScores[currentBranch] = (branchScores[currentBranch] + score())
            }
            else -> {
                current++
                branchScores[currentBranch] = (branchScores[currentBranch] + 1)
            }
        }
    }
    println()
    return branchScores.sortedByDescending { it }.first()
}

fun main(vararg args: String) {
    val score = score()
    println("Final score $score")
}