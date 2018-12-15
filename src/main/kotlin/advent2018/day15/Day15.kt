package advent2018.day15

import java.io.File
import kotlin.math.absoluteValue

data class Dude(val type: Char, var x: Int, var y: Int, var health: Int = 200): Comparable<Dude> {

    fun alive() = health > 0

    override fun compareTo(other: Dude): Int {
        return if (y < other.y || (y == other.y && x < other.x)) -1
        else if (y == other.y && x == other.x) 0
        else 1
    }

}

class Game(fileName: String) {

    private val map: List<CharArray>
    private val dudes: List<Dude>
    init {
        val (map1, dudes1) = loadBoard("testInput.txt")
        map = map1
        dudes = dudes1
    }

    private fun loadBoard(fileName: String): Pair<List<CharArray>, List<Dude>> {
        val map = mutableListOf<CharArray>()
        val dudes = mutableListOf<Dude>()
        File("src/main/resources/day15/$fileName").readLines().forEachIndexed { y, line ->
            val row = CharArray(line.length)
            map.add(row)
            line.forEachIndexed { x, ch ->
                when (ch) {
                    '#', '.' -> row[x] = ch
                    'G', 'E' -> {
                        row[x] = '.'
                        dudes.add(Dude(ch, x, y))
                    }
                }
            }
        }
        return Pair(map, dudes)
    }

    private fun attackable(dude: Dude, targets: List<Dude>): List<Dude> {
        return targets.filter {
            (it.x - dude.x).absoluteValue == 1 && (it.y - dude.y).absoluteValue == 1
        }
    }

    private fun dudeInSpace(x: Int, y: Int, dudes: List<Dude>) =
        dudes.find { it.alive() && it.x == x && it.y == y } != null

    private fun mapIsSpace(x: Int, y: Int, map: List<CharArray>) =
        map[y][x] == '.'

    private fun openSpacesAround(x: Int, y: Int, map: List<CharArray>, dudes: List<Dude>) =
        listOf(
            Pair(x - 1, y),
            Pair(x + 1, y),
            Pair(x, y - 1),
            Pair(x, y + 1)
        ).filter { (x, y) ->
            mapIsSpace(x, y, map) && !dudeInSpace(x, y, dudes)
        }


    private fun inRange(targets: List<Dude>, map: List<CharArray>, dudes: List<Dude>) =
        targets.flatMap {
            openSpacesAround(it.x, it.y, map, dudes)
        }

    private fun routesToTarget(
        x: Int,
        y: Int,
        target: Pair<Int, Int>,
        map: List<CharArray>,
        dudes: List<Dude>,
        soFar: List<Pair<Int, Int>> = emptyList()
    ): List<Pair<Int, Int>> {

        return soFar
    }

    private fun takeARound(dude: Dude, map: List<CharArray>, dudes: List<Dude>): Boolean {
        val targets = dudes.filter { it.alive() && it.type != dude.type }
        if (targets.isEmpty()) return true // game over
        var attackable = attackable(dude, targets)
        if (attackable.isEmpty()) {
            val inRange = inRange(targets, map, dudes)
            val routesToTarget = inRange.map { routesToTarget(dude.x, dude.y, it, map, dudes) }
        }
        return false
    }

    fun round(): Boolean {
        val liveDudes = liveDudes()
        return liveDudes.fold(false) {outcome, dude ->
                outcome || takeARound(dude, map, liveDudes)
        }
    }

    fun liveDudes() = dudes.filter{ it.health > 0 }.sorted()
}

fun main(vararg args: String) {
    val game = Game("testInput.txt")
    var round = 0
    while (round < 5) {
        if (game.round()) break
        round++
    }
    val result = game.liveDudes().map { it.health }.sum() * round
    println("Final result is $result")
}