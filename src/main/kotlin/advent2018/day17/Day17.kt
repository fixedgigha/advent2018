package advent2018.day17

import java.io.File

data class Clay(val x: IntRange, val y: IntRange)

class Game(fileName: String) {

    val map = loadData("testInput.txt")
    val maxY = map.fold(0) { highest, clay -> Math.max(highest, clay.y.last)}
    val waterPoints = mutableSetOf(Pair(500, 0))

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

    fun round(): Int {
        val newPoints = mutableSetOf<Pair<Int, Int>>()
        waterPoints.forEach { point ->
            val below = Pair(point.first, point.second + 1)
            if (below.second < maxY && !waterPoints.contains(below)) {
                if (!inMap(below)) {
                    newPoints.add(below);
                } else {
                    var fillCandidate = point
                    var rising = false
                    while (fillLeft(fillCandidate, newPoints, rising) && fillRight(fillCandidate, newPoints, rising)) {
                        val above = Pair(point.first, point.second - 1)
                        if (waterPoints.contains(above)) {
                            fillCandidate = above
                            rising = true
                        } else {
                            break
                        }
                    }
                }
            }
        }
        newPoints.removeIf { waterPoints.contains(it) }
        waterPoints.addAll(newPoints)
        return newPoints.size
    }

    private fun fillLeft(
        point: Pair<Int, Int>,
        newPoints: MutableSet<Pair<Int, Int>>,
        rising: Boolean = false,
        delta: Int = -1
    ): Boolean {
        var nextPoint = Pair(point.first + delta, point.second)
        var nextPointBelow = Pair(nextPoint.first, nextPoint.second + 1)
        while ((inMap(nextPointBelow) || (rising && newPoints.contains(nextPointBelow))) && !inMap(nextPoint)) {
            newPoints.add(nextPoint)
            nextPoint = Pair(nextPoint.first + delta, point.second)
            nextPointBelow = Pair(nextPoint.first, nextPoint.second + 1)
        }
        return inMap(nextPoint)
    }

    private fun fillRight(
        point: Pair<Int, Int>,
        newPoints: MutableSet<Pair<Int, Int>>,
        rising: Boolean = false
    ): Boolean {
        return fillLeft(point, newPoints, rising, 1)
    }
}

fun main(vararg args: String) {

    val game = Game("testInput.txt")

    println(game.map)
    println("Max Y ${game.maxY}")

    var round = 0
    while (game.round() > 0) {
        round++
        println("Round $round water ${game.waterPoints.size} -> ${game.waterPoints}")
    }
}