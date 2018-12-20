package advent2018.day20

import java.io.File

val text = File("src/main/resources/day20/testInput.txt").readText()
var current = 1
val coords = mutableSetOf(Triple(0, 0, '0'))


fun score(startLoc: Triple<Int, Int, Char>): List<Triple<Int, Int, Char>> {
    val branchScores = mutableListOf<MutableList<Triple<Int, Int, Char>>>(mutableListOf())
    var currentBranch = 0
    var finished = false
    var currLoc = startLoc
    while (!finished) {

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
                branchScores.add(mutableListOf())
            }
            '(' -> {
                current++
                branchScores[currentBranch].addAll(score(currLoc))
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
                branchScores[currentBranch].addAll(newCoords)
                current++
            }
        }
    }
    return branchScores.sortedByDescending { it.size }.first()
}

fun draw() {
    val input = coords.groupBy {Pair(it.first, it.second)}
    val minX = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.first)} - 1
    val minY = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.second)} - 1
    val maxX = input.keys.fold(0) {max, point -> Math.max(max, point.first)} + 1
    val maxY = input.keys.fold(0) {max, point -> Math.max(max, point.second)} + 1

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
    coords.remove(score.last())
    coords.add(score.last().copy(third = 'X'))
    println("Final score ${score.size} ${score.last()}")

    draw()
}