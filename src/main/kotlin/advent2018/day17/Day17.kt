package advent2018.day17

import advent2018.day10.pointsToGrid
import java.io.File

data class Clay(val x: IntRange, val y: IntRange)

class Game(fileName: String) {

    val map = loadData(fileName)
    val maxY = map.fold(0) { highest, clay -> Math.max(highest, clay.y.last)}
    private val minX = map.fold(Int.MAX_VALUE) { lowest, clay -> Math.min(lowest, clay.x.last)}
    private val maxX = map.fold(0) { highest, clay -> Math.max(highest, clay.x.last)}
    val waterPoints = mutableSetOf<Pair<Int, Int>>()
    val alltimeDanglers = mutableSetOf<Pair<Int, Int>>()

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

    private fun inMap(coord: Pair<Int, Int>) =
        map.find { clay -> clay.x.contains(coord.first) && clay.y.contains(coord.second) } != null

    fun round(danglers: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        var newDanglers = mutableSetOf<Pair<Int, Int>>()
        danglers.forEach { point ->
            if (point.second == 60) {
                val z = 1
            }
            val below = Pair(point.first, point.second + 1)
            if (below.second <= maxY) {
                if (!inMap(below)) {
                    if (!alltimeDanglers.contains(below)) {
                        alltimeDanglers.add(below)
                        newDanglers.add(below)
                        waterPoints.add(below)
                    }
                } else {
                    var fillCandidate = point
                    var rising = false
                    var notFinished = true
                    while (notFinished) {
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
                                    notFinished = false
                                }
                            }
                            allPointsFilled.addAll(it.first)
                        }
                        if (notFinished) {
                            val above = allPointsFilled.find {
                                val above = Pair(it.first, it.second - 1)
                                alltimeDanglers.contains(it)
                            }?.copy(second = fillCandidate.second - 1)
                            if (above != null) {
                                fillCandidate = above
                                rising = true
                            } else {
                                notFinished = false
                            }
                        }
                        else {
                            notFinished = false
                        }

                    }
                }
            }
        }
        return newDanglers.toList()
    }

    fun draw(yRange: IntRange = (0 .. maxY)) {
        yRange.forEach {y ->
            ((minX - 2) .. (maxX + 2)).forEach {x ->
                val coord = Pair(x, y)
                print(
                    when {
                        inMap(coord) -> '#'
                        waterPoints.contains(coord) -> '~'
                        else -> '.'
                    }
                )
            }
            println()

        }
    }

    private fun fillLeft(
        point: Pair<Int, Int>,
        rising: Boolean = false,
        delta: Int = -1
    ): Pair<List<Pair<Int, Int>>, Pair<Int, Int>?> {
        if (point.second ==  130) {
            val z = 1
        }
        val addedPoints = mutableListOf<Pair<Int, Int>>()
        var nextPoint = Pair(point.first + delta, point.second)
        var nextPointBelow = Pair(nextPoint.first, nextPoint.second + 1)
        while ((inMap(nextPointBelow) || (rising && waterPoints.contains(nextPointBelow))) && !inMap(nextPoint)) {
            waterPoints.add(nextPoint)
            addedPoints.add(nextPoint)
            nextPoint = Pair(nextPoint.first + delta, point.second)
            nextPointBelow = Pair(nextPoint.first, nextPoint.second + 1)
            if (nextPoint.second ==  130 && nextPoint.first > 530) {
                val z = 1
            }
        }
        if (nextPoint.second == 130) {
            println("$nextPointBelow  ${inMap(nextPointBelow)}  $rising ${waterPoints.contains(nextPointBelow)} $nextPoint ${inMap(nextPoint)}")
        }
        return Pair(addedPoints, when {
            inMap(nextPoint) -> null
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

    println(game.map)
    println("Max Y ${game.maxY}")

    var round = 0
    var danglers = listOf(Pair(500, 0))
    while (danglers.isNotEmpty()) {
        if (round == 89) {
            val z = 1
        }
        danglers = game.round(danglers)
        round++
        println("Round $round water ${game.waterPoints.size} danglers ${danglers.size}") // -> ${game.waterPoints}

        if (round > 236) {
            game.draw((128..145))
        }

    }
    //game.draw()


    println("FINAL RESULT ${game.waterPoints.size}")
}