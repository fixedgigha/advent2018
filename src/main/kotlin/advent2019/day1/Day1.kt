package advent2019.day1

import java.io.File

fun calc(x:  Long): Long {
    return ((x / 3) - 2)
}

fun process(c: Long): Long {
    val x = calc(c)
    return if (x > 0) {

        c + process(x)
    } else {
        c
    }
}
//7632374
fun main() {
    println("Hello")
    val result = File("src/main/resources/2019/day1/input.txt").readLines().fold(0L, {x,  c ->
        x + process(calc(c.toLong()))
    })
    println(result)
}