package advent2018.day25

import java.io.File
import kotlin.math.absoluteValue


data class Coord(val x: Int, val y: Int, val z: Int, val t: Int, var inConstellation: Boolean = false) {

    var constellation = mutableSetOf<Coord>()
    fun distance(other: Coord) = distance(this, other)
}

data class Game(val fileName:  String) {

    val allCoords = mutableListOf<Coord>()

    init {
        allCoords.addAll(File(fileName).readLines().map { line ->
            val points = line.split(",")
            Coord(points[0].toInt(),points[1].toInt(), points[2].toInt(), points[3].toInt())
        })
    }

    fun round1() {
        allCoords.forEach { coord ->
            allCoords.filterNot { it == coord}.forEach { target ->
                if (coord.distance(target) <= 3) {
                    coord.constellation.add(target)
                    target.constellation.add(coord)
                    coord.inConstellation = true
                    target.inConstellation = true
                }
            }
        }
    }

    fun round2() {
        val processedCoords = mutableSetOf<Coord>()
        allCoords.forEach { coord ->
            if (!processedCoords.contains(coord)) {
                coord.constellation.addAll(coord.constellation.flatMap { it.constellation })
                coord.constellation.filterNot { it == coord } .forEach { it.constellation.clear() }
                processedCoords.add(coord)
                processedCoords.addAll(coord.constellation)
            }
        }
    }
}

fun distance(from: Coord, to: Coord): Int =
    (from.x - to.x).absoluteValue +
            (from.y - to.y).absoluteValue +
            (from.z - to.z).absoluteValue +
            (from.t - to.t).absoluteValue



fun main(vararg args: String) {
    val game = Game("src/main/resources/day25/testInput2.txt")
    println("Number of points ${game.allCoords.size}")
    game.round1()
    game.round2()
    val mutipleConstellatios = game.allCoords.filter { it.constellation.isNotEmpty() }
    val singletons = game.allCoords.filterNot { it.inConstellation }

    println("Result - multis ${mutipleConstellatios.size} singles ${singletons.size} - TOTAL - ${mutipleConstellatios.size + singletons.size}")
}