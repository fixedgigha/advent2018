package advent2018

import java.io.File

val inputRegex = Regex("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>")

fun processInput(line: String) =
    inputRegex.matchEntire(line) ?. let { match ->
        Pair(
            Pair(match.groupValues[1].toInt(), match.groupValues[2].toInt()),
            Pair(match.groupValues[3].toInt(), match.groupValues[4].toInt())
        )
    } ?: Pair(Pair(0, 0), Pair(0, 0))

fun pointAfterSecond(initial: Pair<Pair<Int, Int>, Pair<Int, Int>>, seconds: Int) =
    Pair(initial.first.first + (initial.second.first * seconds),
         initial.first.second + (initial.second.second * seconds))

var closest = 0

fun everyPointTouches(points: List<Pair<Int, Int>>): Boolean {
    val pointSet = points.toSet()
    val matches =  points.filter{p ->
        pointSet.contains(Pair(p.first - 1, p.second)) ||
        pointSet.contains(Pair(p.first + 1, p.second)) ||
        pointSet.contains(Pair(p.first, p.second - 1)) ||
        pointSet.contains(Pair(p.first, p.second + 1)) ||
        pointSet.contains(Pair(p.first - 1, p.second - 1 )) ||
        pointSet.contains(Pair(p.first + 1, p.second - 1)) ||
        pointSet.contains(Pair(p.first - 1, p.second + 1)) ||
        pointSet.contains(Pair(p.first + 1, p.second + 1))

    }
    closest = Math.max(closest, matches.size)
    return matches.size == points.size
}

fun pointsToGrid(input: List<Pair<Int, Int>>)  {
    val maxXY = input.fold(Pair(0, 0)) { current, latest ->
        Pair(Math.max(current.first, latest.first), Math.max(current.second, latest.second))
    }
    val minXY = input.fold(Pair(maxXY.first, maxXY.second)) { current, latest ->
        Pair(Math.min(current.first, latest.first), Math.min(current.second, latest.second))
    }
    val setPoints = input.toSet()

    File("out/day10.out").bufferedWriter().use { writer ->
        for (y in minXY.second..maxXY.second) {
            for (x in minXY.first..maxXY.first) {
                writer.append(if (setPoints.contains(Pair(x, y))) '#' else '.')
            }
            writer.newLine()
            writer.flush()
        }
    }
}


fun main(args: Array<String>) {
    val coords = File("src/main/resources/day10/input.txt").readLines().map { processInput(it)}

    for (n in 1 .. 100000) {
        val points = coords.map { co -> pointAfterSecond(co, n) }
        if (everyPointTouches(points)) {
            println(n)
            pointsToGrid(points)
            break
        }
    }
    println("$closest -- ${coords.size}")
}