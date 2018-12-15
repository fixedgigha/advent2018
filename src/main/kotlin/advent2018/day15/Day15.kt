package advent2018.day15

import java.io.File

data class Dude(val type: Char, var x: Int, var y: Int, var health: Int = 200): Comparable<Dude> {
    override fun compareTo(other: Dude): Int {
        return if (y < other.y || (y == other.y && x < other.x)) -1
        else if (y == other.y && x == other.x) 0
        else 1
    }

}

fun loadBoard(fileName: String): Pair<List<CharArray>, List<Dude>> {
    val map = mutableListOf<CharArray>()
    val dudes = mutableListOf<Dude>()
    File("src/main/resources/day15/$fileName").readLines().forEachIndexed {y, line ->
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

fun takeARound(dude: Dude, map: List<CharArray>, dudes: List<Dude>): Boolean {

    return false
}

fun main(vararg args: String) {
    val (map, dudes) = loadBoard("testInput.txt")
    var round = 0
    while (round < 5) {
        val liveDudes = dudes.filter{ it.health > 0 }.sorted()
        if (liveDudes.fold(false) {outcome, dude ->
            outcome || takeARound(dude, map, liveDudes)
        }) break
        round++
    }
    val result = dudes.filter { it.health > 0 }.map { it.health }.sum() * round
    println("Final result is $result")
}