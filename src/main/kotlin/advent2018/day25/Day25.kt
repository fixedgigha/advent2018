package advent2018.day25

import java.io.File


data class Coord(val x: Int, val y: Int, val z: Int, val t: Int)

data class Game(val fileName:  String) {

    val allCoords: Set<Coord>

    init {
        allCoords = File(fileName).readLines().map {line ->
            val points = line.split(",")
            Coord(points[0].toInt(),points[1].toInt(), points[2].toInt(), points[3].toInt())
        }.toSet()
    }
}


fun main(vararg args: String) {
    val game = Game("src/main/resources/day25/testInput1.txt")
    println("Number of points ${game.allCoords.size}")
}