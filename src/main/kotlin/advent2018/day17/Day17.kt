package advent2018.day17

import java.io.File

data class Clay(val x: IntRange, val y: IntRange)

class Game(fileName: String) {

    val map = loadData(fileName)
    val maxY = map.fold(0) { highest, clay -> Math.max(highest, clay.y.last)}
    private val minX = map.fold(Int.MAX_VALUE) { lowest, clay -> Math.min(lowest, clay.x.last)}
    private val maxX = map.fold(0) { highest, clay -> Math.max(highest, clay.x.last)}
    val waterPoints = mutableSetOf<Pair<Int, Int>>()
    private val alltimeDanglers = mutableSetOf<Pair<Int, Int>>()
    private val fillingDanglers = mutableSetOf<Pair<Int, Int>>()
    val filledPoints = mutableSetOf<Pair<Int, Int>>()

    private fun makeRange(input: String): IntRange =
        Regex("(\\d+)\\.\\.(\\d+)").matchEntire(input)?.let {
            IntRange(it.groupValues[1].toInt(), it.groupValues[2].toInt())
        } ?: IntRange(input.toInt(), input.toInt())


    private fun loadData(fileName: String) =
        File("src/main/resources/day17/$fileName").readLines().map { line ->
            Regex("(x|y)=(\\d+(\\.\\.\\d+)?)").findAll(line)
                .map { match -> match.groupValues[1] to makeRange(match.groupValues[2]) }
                .groupBy { it.first }
                .let { map ->
                    Clay(
                        map.getOrDefault("x", emptyList()).first().second,
                        map.getOrDefault("y", emptyList()).first().second
                    )
                }
        }.toSet()

    private fun inClay(coord: Pair<Int, Int>) =
        map.find { clay -> clay.x.contains(coord.first) && clay.y.contains(coord.second) } != null

    private fun findAbove(points: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        if (points.isEmpty()) return emptyList()
        val sorted = mutableListOf<Pair<Int, Int>>()
        sorted.addAll(points.sortedBy { it.first })
        while (waterPoints.contains(Pair(sorted.first().first - 1, sorted.first().second ))) {
            sorted.add(0, Pair(sorted.first().first - 1, sorted.first().second ))
        }
        while (waterPoints.contains(Pair(sorted.last().first + 1, sorted.last().second ))) {
            sorted.add(Pair(sorted.last().first + 1, sorted.last().second ))
        }
        return sorted.map { Pair(it.first, it.second - 1) }.filter { alltimeDanglers.contains(it) }
    }

    fun round(danglers: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        var newDanglers = mutableSetOf<Pair<Int, Int>>()
        danglers.forEach { point ->
            val below = Pair(point.first, point.second + 1)
            if (below.second <= maxY) {
                if (!inClay(below)) {
                    if (!alltimeDanglers.contains(below)) {
                        alltimeDanglers.add(below)
                        newDanglers.add(below)
                        waterPoints.add(below)
                    }
                } else {
                    var fillCandidates = mutableSetOf(point)
                    var rising = false

                    while (fillCandidates.isNotEmpty()) {
                        val fillCandidate = fillCandidates.take(1).first()
                        fillCandidates.remove(fillCandidate)
                        fillingDanglers.add(fillCandidate)
                        var notFinished = true
                        if (fillCandidate.second == 55) {
                            val z = 1
                        }
                        val allPointsFilled = mutableListOf<Pair<Int, Int>>(fillCandidate)
                        listOf (
                            fillLeft(fillCandidate, rising),
                            fillRight(fillCandidate, rising)
                        ).forEach {
                            val potentialDangler = it.second
                            if (potentialDangler != null) {
                                if (!alltimeDanglers.contains(potentialDangler)) {
                                    alltimeDanglers.add(potentialDangler)
                                    newDanglers.add(potentialDangler)
                                    waterPoints.add(potentialDangler)
                                }
                                notFinished = false
                            }
                            allPointsFilled.addAll(it.first)
                        }
                        if (notFinished) {
                            filledPoints.addAll(allPointsFilled)
                            val above = findAbove(allPointsFilled)
                            if (above.isNotEmpty()) {
                                fillCandidates.addAll(above.toSet())
                                rising = true
                            }
                        }

                    }
                }
            }
        }
        return newDanglers.toList()
    }

    fun draw(yRange: IntRange = (0 .. maxY)) {
        val xRange = (minX - 2) .. (maxX + 2)
        print("    ")
        xRange.forEach { x ->
            print(if (x % 10 == 0) "${x / 100}"  else " ")
        }
        println()
        print("    ")
        xRange.forEach { x ->
            print(if (x % 10 == 0) "${(x / 10) % 10 }"  else " ")
        }
        println()
        print("    ")
        xRange.forEach { x ->
            print(x % 10)
        }
        println()
        yRange.forEach {y ->
            print("%04d".format(y))
            xRange.forEach {x ->
                val coord = Pair(x, y)
                print(
                    when {
                        inClay(coord) -> '#'
                        filledPoints.contains(coord) -> '~'
                        waterPoints.contains(coord) -> '|'
                        else -> '.'
                    }
                )
            }
            println()

        }
    }

    private fun debug(message: String = "DEBUG:", predicate: () -> Boolean = {true}, yRange: IntRange = (0..maxY)): Boolean {

        if (predicate()) {
            println(message)
            draw(yRange)
        }
        return true
    }

    private fun fallingDangler(candidate: Pair<Int, Int>) = alltimeDanglers.contains(candidate) && !fillingDanglers.contains(candidate)

    private fun fillLeft(
        point: Pair<Int, Int>,
        rising: Boolean = false,
        delta: Int = -1
    ): Pair<List<Pair<Int, Int>>, Pair<Int, Int>?> {

        val addedPoints = mutableListOf<Pair<Int, Int>>()
        var nextPoint = Pair(point.first + delta, point.second)
        var nextPointBelow = Pair(nextPoint.first, nextPoint.second + 1)
        while ((inClay(nextPointBelow) || (rising && waterPoints.contains(nextPointBelow))) && (!fallingDangler(nextPoint)) && (!inClay(nextPoint))) {
            waterPoints.add(nextPoint)
            addedPoints.add(nextPoint)
            nextPoint = Pair(nextPoint.first + delta, point.second)
            nextPointBelow = Pair(nextPoint.first, nextPoint.second + 1)
            if (nextPoint.second ==  51 && nextPoint.first == 502) {
                val z = 1
            }
        }
        return Pair(addedPoints, when {
            inClay(nextPoint)  -> null
            else -> nextPoint // we haven't stopped because we hit a wall, we stopped because we're about to overflow
        })
    }

    private fun fillRight(
        point: Pair<Int, Int>,
        rising: Boolean = false
    ): Pair<List<Pair<Int, Int>>, Pair<Int, Int>?> {

        return fillLeft(point, rising, 1)
    }
}

fun main(vararg args: String) {

    val game = Game("input.txt")

    println("Max Y ${game.maxY}")

    val minY = game.map.fold(game.maxY) { max, clay -> Math.min(max, clay.y.first)}
    var round = 0
    var danglers = listOf(Pair(500, minY - 1))
    var yMax = 0
    while (danglers.isNotEmpty()) {

        danglers = game.round(danglers)
        round++
        yMax = game.waterPoints.fold(yMax) { max, point -> Math.max(max, point.second)}
        //println("Round $round water ${game.waterPoints.size} danglers ${danglers.size} y $yMax") // -> ${game.waterPoints}


    }
    game.draw()
    println("Min Y $minY")
    println("FINAL RESULT ${game.waterPoints.size}")
    println("Filled ${game.filledPoints.size}")
}