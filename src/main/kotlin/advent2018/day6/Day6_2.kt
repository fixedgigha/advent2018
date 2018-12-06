package advent2018.day6

import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    var (coords, maxCoords) = File("src/main/resources/day6/input.txt")
        .readLines()
        .fold(Pair(mutableListOf<Pair<Int, Int>>(), Pair(0, 0))) { (list, maxCoords), line ->
            val mr = lineRegex.matchEntire(line)
            val newMaxCoords = if (mr != null) {
                val x = mr.groupValues[1].toInt()
                val y = mr.groupValues[2].toInt()
                list.add(Pair(x, y))
                Pair(Math.max(x, maxCoords.first), Math.max(y, maxCoords.second))
            }
            else {
                maxCoords
            }
            Pair(list, newMaxCoords)
        }
    var count = 0
    val maxX = maxCoords.first + 1
    val maxY = maxCoords.second + 1
    for (x in 0..maxX)
        for (y in 0..maxY) {
            val thisCoord = Pair(x, y)
            count += if (scoreCoord(thisCoord, coords) < 10000) 1 else 0
        }
   println("Winner $count")
}

fun scoreCoord(thisCoord: Pair<Int, Int>, coords: MutableList<Pair<Int, Int>>): Int =
        coords.fold(0) { currentScore, coord ->
            val score = (thisCoord.first - coord.first).absoluteValue + (thisCoord.second - coord.second).absoluteValue
            score + currentScore
        }
