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
    val sortedBranches = branchScores.sortedByDescending { it.size }
    // Do I care about equivalently long branches?
    return sortedBranches.first()
}

fun draw() {
    val input = coords.groupBy {Pair(it.first, it.second)}.mapValues { (k, v) -> v.first().third }
    val minX = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.first)} - 1
    val minY = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.second)} - 1
    val maxX = input.keys.fold(0) {max, point -> Math.max(max, point.first)} + 1
    val maxY = input.keys.fold(0) {max, point -> Math.max(max, point.second)} + 1

    (minY..maxY).forEach {y ->
        (minX..maxX).forEach {x ->
            val myPoint = Pair(x, y)
            print(
                if (input.contains(myPoint)) {
                    input[myPoint]?:'?'
                }
                else {
                    '#'
                }
            )
        }
        println()
    }
}

val dummyPair = Pair(Int.MIN_VALUE, Int.MIN_VALUE)

fun candidatesFrom(course: Map<Pair<Int, Int>, Char>,
                   point: Pair<Int, Int>,
                   routeSoFar: Set<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val candidates = mutableListOf<Pair<Int, Int>>()
    val pointWest = point.copy(first = point.first - 2)
    val doorWest  = point.copy(first = point.first - 1)
    val moveWest  = course[pointWest]
    if (moveWest != null && !routeSoFar.contains(pointWest) && moveWest == '.' &&
            course[doorWest]?:'~' == '|') {
        candidates.add(pointWest)
    }
    val pointEast = point.copy(first = point.first + 2)
    val doorEast  = point.copy(first = point.first + 1)
    val moveEast  = course[pointEast]
    if (moveEast != null && !routeSoFar.contains(pointEast) && moveEast == '.' &&
        course[doorEast]?:'~' == '|') {
        candidates.add(pointEast)
    }
    val pointNorth = point.copy(first = point.second - 2)
    val doorNorth  = point.copy(first = point.second - 1)
    val moveNorth  = course[pointNorth]
    if (moveNorth != null && !routeSoFar.contains(pointNorth) && moveNorth == '.' &&
        course[doorNorth]?:'~' == '|') {
        candidates.add(pointNorth)
    }
    val pointSouth = point.copy(first = point.second + 2)
    val doorSouth  = point.copy(first = point.second + 1)
    val moveSouth  = course[pointSouth]
    if (moveSouth != null && !routeSoFar.contains(pointSouth) && moveSouth == '.' &&
        course[doorSouth]?:'~' == '|') {
        candidates.add(pointSouth)
    }

    return candidates
}

fun route(course: Map<Pair<Int, Int>, Char>,
          target: Pair<Int, Int>,
          noLongerThan: Int = Int.MAX_VALUE,
          start: Pair<Int, Int> = Pair(0, 0),
          routeSoFar: Set<Pair<Int, Int>> = setOf())  {
    val myRoute = mutableListOf(start)
    val soFar = mutableSetOf<Pair<Int, Int>>()
    soFar.addAll(routeSoFar)
    var current = start
    while (myRoute.size < noLongerThan && current != target) {

        val candidates = candidatesFrom(course, current, soFar)
    }

}

fun main(vararg args: String) {
    val score = score(coords.last())
    coords.remove(score.last())
    coords.add(score.last().copy(third = 'X'))
    println("Final score ${score.size} ${score.last()}")

    draw()
}