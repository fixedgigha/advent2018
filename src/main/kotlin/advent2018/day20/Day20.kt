package advent2018.day20
import java.io.File

val text = File("src/main/resources/day20/input.txt").readText()
var current = 1

val pointScores = mutableMapOf<Pair<Int, Int>, Int>()

fun adjust(point: Pair<Int, Int>, dir: Char) =
    when(dir) {
        'W' -> point.copy(first = point.first - 1)
        'E' -> point.copy(first = point.first + 1)
        'N' -> point.copy(second = point.second - 1)
        'S' -> point.copy(second = point.second + 1)
        else -> point
    }

fun loadNodes(fromPoint: Pair<Int, Int>, endChar: Char = ')') {
    var finished = false
    var curPoint = fromPoint
    while (!finished) {

        when(text[current]) {
            endChar -> {
                current++
                finished = true
             }
            '|' -> {
                current++
                curPoint = fromPoint
            }
            '(' -> {
                current++
                loadNodes(curPoint)
            }
            else -> {
                val newPoint = adjust(curPoint, text[current])
                val incrementPrevious = (pointScores[curPoint]?:0) + 1
                pointScores[newPoint] = Math.min(incrementPrevious, pointScores[newPoint]?:Int.MAX_VALUE)
                curPoint = newPoint
                current++
            }
        }
    }
}

fun part1Total(): Int =
    pointScores.values.max()?:0

fun part2Total(): Int =
    pointScores.values.filter { it > 1000}.size


fun main(vararg args: String) {
    loadNodes(Pair(0, 0), '$')

    val part1Total = part1Total()

    println("Part 1 $part1Total")

    val part2Total = part2Total()

    println("Part 2 $part2Total")

}