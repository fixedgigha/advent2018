package advent2018.day17

import java.io.File

data class Clay(val x: IntRange, val y: IntRange)

fun makeRange(input: String): IntRange =
    Regex("(\\d+)\\.\\.(\\d+)").matchEntire(input)?.let {
        IntRange(it.groupValues[1].toInt(), it.groupValues[2].toInt())
    }?: IntRange(input.toInt(), input.toInt())


fun loadData(fileName: String) =
    File("src/main/resources/day17/$fileName").readLines().map{line ->
        Regex("(x|y)=(\\d+|\\d+\\.\\.\\d+)").findAll(line)
            .map { match -> match.groupValues[1] to makeRange(match.groupValues[2]) }
            .groupBy { it.first }
            .let {map ->
                Clay(
                    map.getOrDefault("x", emptyList()).first().second,
                    map.getOrDefault("y", emptyList()).first().second
                )
            }
    }


fun main(vararg args: String) {
    val map = loadData("testInput.txt")
    println(map)

}