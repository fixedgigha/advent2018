package advent2018.day6

import java.io.File
import kotlin.math.absoluteValue

val lineRegex = Regex("(\\d+), (\\d+)")

fun main(args: Array<String>) {
    var (coords, maxCoords) = File("src/main/resources/day6/input.txt")
        .readLines()
        .fold(Pair(mutableListOf<Pair<Int, Int>>(), Pair(0, 0))) { (list, maxCoords), line ->
            val newMaxCoords =
                lineRegex.matchEntire(line) ?. let { mr ->
                    val x = mr.groupValues[1].toInt()
                    val y = mr.groupValues[2].toInt()
                    list.add(Pair(x, y))
                    Pair(Math.max(x, maxCoords.first), Math.max(y, maxCoords.second))
                } ?: maxCoords
            Pair(list, newMaxCoords)
        }
    val grid = mutableMapOf<Pair<Int, Int>, Int>()
    val maxX = maxCoords.first + 1
    val maxY = maxCoords.second + 1
    for (x in 0..maxX)
        for (y in 0..maxY) {
            val thisCoord = Pair(x, y)
            grid[thisCoord] = nearestCoord(thisCoord, coords)
        }
    val infinities = grid.entries.filter {
        it.key.first == 0 || it.key.second == 0 || it.key.first == maxX || it.key.second == maxY
    }
    .map { it.value } .distinct()

    val winner = grid.values.filter { it != tieMarker && !infinities.contains(it)}.groupBy { it }.values.map {it.size}.sortedDescending().first()
    println("Winner $winner")
}

const val tieMarker = -1

fun nearestCoord(thisCoord: Pair<Int, Int>, coords: MutableList<Pair<Int, Int>>): Int {
    val (closest, closestScore) =
        coords.foldIndexed(Pair(tieMarker, Int.MAX_VALUE)) {index, (currentClosest, currentClosestScore), coord ->
            val score = (thisCoord.first - coord.first).absoluteValue + (thisCoord.second - coord.second).absoluteValue
            val thisWins = score < currentClosestScore
            val thisTies = score == currentClosestScore
            Pair(
                if (thisWins) index else if (thisTies) tieMarker           else currentClosest,
                if (thisWins) score else if (thisTies) currentClosestScore else currentClosestScore
            )
        }
    return closest
}
