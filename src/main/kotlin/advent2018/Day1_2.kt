package advent2018

import java.io.File

fun main(args : Array<String>) : Unit {
    val lines = File("src/main/resources/day1/input.txt").readLines()
    val freqs = mutableSetOf<Int>()
    var found: Int? = null
    var current = 0

    while(found ==null) {
        lines.forEach {
            if (found == null) {
                current += it.toInt()
                if (freqs.contains(current)) {
                    found = current
                } else {
                    freqs.add(current)
                }
            }
        }
    }
    println("" + found)

}
