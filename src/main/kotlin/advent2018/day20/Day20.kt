package advent2018.day20

import java.io.File

val text = File("src/main/resources/day20/input.txt").readText()
var current = 1

fun score(): List<Char> {
    val branchScores = mutableListOf<MutableList<Char>>(mutableListOf())
    var currentBranch = 0
    var finished = false
    while (!finished) {
        print(text[current])
        when(text[current]) {
            '$', ')' -> {
                current++
                finished = true
             }
            '|' -> {
                current++
                currentBranch++
                branchScores.add(mutableListOf())
            }
            '(' -> {
                current++
                branchScores[currentBranch].addAll(score())
            }
            else -> {
                branchScores[currentBranch].add(text[current])
                current++
            }
        }
    }
    println()
    return branchScores.sortedByDescending { it.size }.first()
}

fun draw(input: List<Pair<Int, Int>>) {
    val minX = input.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.first)}
    val minY = input.fold(Int.MAX_VALUE) {min, point -> Math.min(min, point.second)}
    val maxX = input.fold(0) {max, point -> Math.max(max, point.first)}
    val maxY = input.fold(0) {max, point -> Math.max(max, point.second)}

    (minY..maxY).forEach {y ->
        (minX..maxX).forEach {x ->
            val myPoint = Pair(x, y)
            print(
                if (input.contains(myPoint)) {
                    when (myPoint) {
                        input.last() -> 'X'
                        Pair(0, 0) -> '0'
                        else -> '.'
                    }
                }
                else {
                    '#'
                }
            )
        }
        println()
    }
}

fun main(vararg args: String) {
    val score = score()
    println("Final score ${score.size}")
    val coords = mutableListOf(Pair(0, 0))
    score.forEach { c ->
        val lastPoint = coords.last()
        coords.addAll((1..2).map {a ->
            when(c) {
                'N' -> lastPoint.copy(second = lastPoint.second - a)
                'S' -> lastPoint.copy(second = lastPoint.second + a)
                'W' -> lastPoint.copy(first = lastPoint.first - a)
                'E' -> lastPoint.copy(first = lastPoint.first + a)
                else -> lastPoint
            }}
        )
    }
    draw(coords)
}