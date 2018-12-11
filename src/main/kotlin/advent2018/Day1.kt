package advent2018

import java.io.File

fun main(args: Array<String>): Unit {
    val count = File("src/main/resources/day1/input.txt").readLines()
        .map { it.toInt() }
        .sum()
s
    println("Hello $count")
}
