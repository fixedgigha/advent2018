package advent2018.day25

import java.io.File
import kotlin.math.absoluteValue


data class Coord(val x: Int, val y: Int, val z: Int, val t: Int) {
    var inConstellation: Boolean = false
    var constellation = mutableSetOf<Coord>()
    fun distance(other: Coord) = distance(this, other)
}

data class Game(val fileName:  String) {

    val allCoords = mutableListOf<Coord>()

    init {
        allCoords.addAll(File(fileName).readLines().map { line ->
            val points = line.split(",")
            Coord(points[0].toInt(), points[1].toInt(), points[2].toInt(), points[3].toInt())
        })
    }

    /**
     * For each point find all *in distance* other points
     */
    fun findNeighbours() {
        allCoords.forEach { coord ->
            allCoords.filterNot { it == coord }.forEach { target ->
                if (coord.distance(target) <= 3) {
                    coord.constellation.add(target)
                    target.constellation.add(coord)
                    coord.inConstellation = true
                    target.inConstellation = true
                }
            }
        }
    }

    private fun collectChildren(from: Coord, into: MutableSet<Coord>) {
        val additions = from.constellation.filterNot { into.contains(it) }
        if (additions.isNotEmpty()) {
            into.addAll(additions)
            additions.forEach { collectChildren(it, into) }
        }
    }

    val multiConstellations = mutableSetOf<Set<Coord>>()

    /**
     * Merge > 1 constellations from round 1 into full constellations
     */
    fun mergeConstellations() {
        val alreadyProcessed = mutableListOf<Coord>()
        allCoords.forEach { coord ->
            if (coord.inConstellation && !alreadyProcessed.contains(coord)) {
                alreadyProcessed.add(coord)
                val iterSet = mutableSetOf<Coord>()
                iterSet.addAll(coord.constellation)
                iterSet.forEach {  collectChildren(it, coord.constellation) }
                alreadyProcessed.addAll(coord.constellation)
                multiConstellations.add(coord.constellation)
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
    val game = Game("src/main/resources/day25/input.txt")
    println("Number of points ${game.allCoords.size}")
    game.findNeighbours()
    game.mergeConstellations()
    val mutipleConstellatios = game.multiConstellations
    val singletons = game.allCoords.filterNot { it.inConstellation }

    println("Result - multis ${mutipleConstellatios.size} singles ${singletons.size} - TOTAL - ${mutipleConstellatios.size + singletons.size}")
}