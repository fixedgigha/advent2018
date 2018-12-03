package advent2018

import java.io.File


fun charCounts(charArr : CharSequence, targets: Set<Int> = setOf(2, 3)) =
    charArr.groupBy { it }
        .values
        .map { it.size }
        .filter { targets.contains(it) }
        .distinct()

fun countMapList(key: Int, map: Map<Int, List<Int>>) = (map[key]?:emptyList()).size

fun main(args : Array<String>) : Unit {
    val count = File("src/main/resources/day2/input.txt").readLines()
        .flatMap { charCounts(it) }
        .groupBy { it }

    println("Result ${countMapList(2, count) * countMapList(3, count)}")
}
