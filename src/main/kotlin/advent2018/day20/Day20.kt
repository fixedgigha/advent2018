package advent2018.day20

import java.io.File

val text = File("src/main/resources/day20/input.txt").readText()
var current = 1
val coords = mutableListOf(Triple(0, 0, '0'))


fun score(startLoc: Triple<Int, Int, Char>): Int {
    val branchScores = mutableListOf(0)
    var currentBranch = 0
    var finished = false
    var currLoc = startLoc
    while (!finished) {
        print(text[current])

        when(text[current]) {
            '$'-> {
                finished = true
            }
            ')' -> {
                current++
                finished = true
             }
            '|' -> {
                currLoc = startLoc
                current++
                currentBranch++
                branchScores.add(0)
            }
            '(' -> {
                current++
                branchScores[currentBranch] = branchScores[currentBranch] + score(currLoc)
            }
            else -> {
                val newCoords = (1..2).map { a ->
                    val m = if (a == 1) '|' else '.'
                    when (text[current]) {
                        'N' -> currLoc.copy(second = currLoc.second - a, third = m)
                        'S' -> currLoc.copy(second = currLoc.second + a, third = m)
                        'W' -> currLoc.copy(first = currLoc.first - a, third = m)
                        'E' -> currLoc.copy(first = currLoc.first + a, third = m)
                        else -> currLoc
                    }
                }
                coords.addAll(newCoords)
                currLoc = newCoords.last()
                branchScores[currentBranch] += 1
                current++
            }
        }
    }
    println()
    return branchScores.sortedByDescending { it }.first()
}

fun draw() {
    val input = coords.groupBy {Pair(it.first, it.second)}
    val minX = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.first)}
    val minY = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.second)}
    val maxX = input.keys.fold(0) {max, point -> Math.max(max, point.first)}
    val maxY = input.keys.fold(0) {max, point -> Math.max(max, point.second)}

    (minY..maxY).forEach {y ->
        (minX..maxX).forEach {x ->
            val myPoint = Pair(x, y)
            print(
                if (input.contains(myPoint)) {
                    input[myPoint]?.first()?.third?:'?'
                }
                else {
                    '#'
                }
            )
        }
        println()
    }
}

fun main(vararg args: String) {
    val score = score(coords.last())
    println("Final score $score")

    draw()
}