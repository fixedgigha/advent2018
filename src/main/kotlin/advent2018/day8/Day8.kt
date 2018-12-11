package advent2018.day8

import java.io.File

fun process(input: MutableList<Int>): Int {
    val childCount = input.removeAt(0)
    val metaCount = input.removeAt(0)

    return  (1 .. childCount).fold(0) {total, _ -> total + process(input) } +
            (1 .. metaCount) .fold(0) {total, _ -> total + input.removeAt(0) }
}

fun main(args: Array<String>) {
    val input = mutableListOf<Int>()
    input.addAll(
        File("src/main/resources/day8/input.txt").readText().split(" ").map { it.toInt() }
    )
    println(process(input))

}