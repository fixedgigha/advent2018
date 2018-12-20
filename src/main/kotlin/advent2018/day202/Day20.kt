package advent2018.day202

import java.io.File

val text = File("src/main/resources/day20/input.txt").readText()
var current = 1
val coords = mutableSetOf(Triple(0, 0, '0'))

val ANSI_RED = "\u001B[31m"
val ANSI_RESET = "\u001B[0m"

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

fun draw(input: Map<Pair<Int, Int>, Char>, route: List<Pair<Int, Int>>) {
    val routeSet = route.toSet()

    val minX = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.first)} - 1
    val minY = input.keys.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.second)} - 1
    val maxX = input.keys.fold(0) {max, point -> Math.max(max, point.first)} + 1
    val maxY = input.keys.fold(0) {max, point -> Math.max(max, point.second)} + 1

    (minY..maxY).forEach {y ->
        (minX..maxX).forEach {x ->
            val myPoint = Pair(x, y)
            print(
                if (routeSet.contains(myPoint)) {
                    "$ANSI_RED~$ANSI_RESET"
                }
                else if (input.contains(myPoint)) {
                    (input[myPoint]?:'?').toString()
                }
                else {
                    '#'.toString()
                }
            )
        }
        println()
    }
}

val steps = setOf('0', '.', 'X')

fun candidatesFrom(course: Map<Pair<Int, Int>, Char>,
                   point: Pair<Int, Int>,
                   routeSoFar: Set<Pair<Int, Int>>): List<Pair<Int, Int>> {
    val candidates = mutableListOf<Pair<Int, Int>>()
    val pointWest = point.copy(first = point.first - 2)
    val doorWest  = point.copy(first = point.first - 1)
    val moveWest  = course[pointWest]
    if (moveWest != null && (!routeSoFar.contains(pointWest)) && steps.contains(moveWest) &&
            course[doorWest]?:'~' == '|') {
        candidates.add(pointWest)
    }
    val pointEast = point.copy(first = point.first + 2)
    val doorEast  = point.copy(first = point.first + 1)
    val moveEast  = course[pointEast]
    if (moveEast != null && (!routeSoFar.contains(pointEast)) && steps.contains(moveEast) &&
        course[doorEast]?:'~' == '|') {
        candidates.add(pointEast)
    }
    val pointNorth = point.copy(second = point.second - 2)
    val doorNorth  = point.copy(second = point.second - 1)
    val moveNorth  = course[pointNorth]
    if (moveNorth != null && (!routeSoFar.contains(pointNorth)) && steps.contains(moveNorth) &&
        course[doorNorth]?:'~' == '|') {
        candidates.add(pointNorth)
    }
    val pointSouth = point.copy(second = point.second + 2)
    val doorSouth  = point.copy(second = point.second + 1)
    val moveSouth  = course[pointSouth]
    if (moveSouth != null && (!routeSoFar.contains(pointSouth)) && steps.contains(moveSouth) &&
        course[doorSouth]?:'~' == '|') {
        candidates.add(pointSouth)
    }

    return candidates
}

fun route(course: Map<Pair<Int, Int>, Char>,
          start: Pair<Int, Int> = Pair(0, 0),
          routeSoFar: Set<Pair<Int, Int>> = setOf()): List<List<Pair<Int, Int>>>  {
    val myRoute = mutableListOf(start)
    val soFar = mutableSetOf(start)
    soFar.addAll(routeSoFar)
    var current = start
    while (true) {
        val candidates = candidatesFrom(course, current, soFar)
        when (candidates.size) {
            1 -> {
                current = candidates.first()
                myRoute.add(current)
                soFar.add(current)
            }
            0-> {
                return listOf(myRoute)
            }
            else -> {
                return candidates.flatMap {can ->
                    route(course, can, soFar)
                }.map { newRoute ->
                    val superRoute = mutableListOf<Pair<Int, Int>>()
                    superRoute.addAll(myRoute)
                    superRoute.addAll(newRoute)
                    superRoute
                }
            }
        }
    }
   // unreachab;e!!
    println("WARN unreachable $start")
    return emptyList()

}

fun main(vararg args: String) {
    val score = score(coords.last())
    val target = score.last()
    coords.remove(target)
    coords.add(target.copy(third = 'X'))
    println("Final score ${score.size} ${score.last()}")
    val course = coords.groupBy {Pair(it.first, it.second)}.mapValues { (k, v) -> v.first().third }
    val routes = route(course)

    val routeGroups = routes.groupBy {it.last()}
    println("Route groups ${routeGroups.size}")
    val guesses = routeGroups.values.map { it.sortedBy { it.size } }.filter {
        it.first().size > 1001
    }
    val longestRoute = guesses.fold(Pair(0, 0)) { (max, min), next ->
        val innerMax = next.fold(max) { inMax, inNext ->
            Math.max(inMax, inNext.size)
        }
        if (innerMax > max) {
            Pair(innerMax, next.first().size)
        }
        else {  Pair(max, min) }
    }

    //draw(course, guesses.first().first())
    println("My guess ${guesses.size}  longest route $longestRoute")
}