package advent2018.day17

import java.io.File

data class Clay(val x: IntRange, val y: IntRange)

fun makeRange(input: String): IntRange =
    Regex("(\\d+)\\.\\.(\\d+)").matchEntire(input)?.let {
        IntRange(it.groupValues[1].toInt(), it.groupValues[2].toInt())
    }?: IntRange(input.toInt(), input.toInt())


fun loadData(fileName: String) =
    File("src/main/resources/day17/$fileName").readLines().map{line ->
        Regex("(x|y)=(\\d+(\\.\\.\\d+)?)").findAll(line)
            .map { match -> match.groupValues[1] to makeRange(match.groupValues[2]) }
            .groupBy { it.first }
            .let {map ->
                Clay(
                    map.getOrDefault("x", emptyList()).first().second,
                    map.getOrDefault("y", emptyList()).first().second
                )
            }
    }.toSet()

fun inMap(coord: Pair<Int, Int>, map: Set<Clay>) =
        map.find { clay -> clay.x.contains(coord.first) && clay.y.contains(coord.second)} != null

fun round(map: Set<Clay>, waterPoints: MutableSet<Pair<Int, Int>>, maxY: Int): Int {
    val newPoints = mutableSetOf<Pair<Int, Int>>()
    waterPoints.forEach {point ->
        val below = Pair(point.first, point.second + 1)
        if (below.second < maxY && !waterPoints.contains(below)) {
            if (! inMap(below, map)) {
                newPoints.add(below);
            }
        }
    }
    waterPoints.addAll(newPoints)
    return newPoints.size
}

fun main(vararg args: String) {
    val map = loadData("testInput.txt")
    val maxY = map.fold(0) { highest, clay -> Math.max(highest, clay.y.last)}
    println(map)
    println("Max Y $maxY")

    val waterPoints = mutableSetOf(Pair(500, 0))
    var round = 0
    while (round(map, waterPoints, maxY) > 0) {
        round++
        println("Round $round water ${waterPoints.size} -> $waterPoints")
    }
}