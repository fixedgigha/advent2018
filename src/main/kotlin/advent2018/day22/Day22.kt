package advent2018.day22

import kotlin.math.absoluteValue

val target = Pair(10, 10)
const val depth = 510
var currentFastest = Int.MAX_VALUE

fun moves(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    result.add(point.copy(first = point.first - 1))
    result.add(point.copy(first = point.first + 1))
    result.add(point.copy(second = point.second - 1))
    result.add(point.copy(second = point.second + 1))

    return result.filter { (x, y) -> x >= 0 && y >= 0 && x <= target.first + 8 && y <= target.second + 8}
}

fun goodMatch(tool: Char): List<Int> =
    when (tool) {
        't' -> listOf(0, 2)
        'c' -> listOf(0, 1)
        'n' -> listOf(1, 2)
        else -> emptyList()
    }

fun toTarget(point: Pair<Int, Int>) =
    (target.first - point.first).absoluteValue + (target.second - point.second).absoluteValue

fun toolsForMove(point: Pair<Int, Int>) =
    when(cellScore(point)) {
        0 -> listOf('c', 't')
        1 -> listOf( 'n', 'c')
        else -> listOf('t', 'n')
    }

data class Route(
    var current: Pair<Int, Int> = Pair(0, 0),
    var score: Int = 0,
    var tool: Char = 't',
    val soFar: MutableSet<Pair<Int, Int>> = mutableSetOf()) {

    fun route(): Route {
        val easyRoutes = mutableListOf<Route>()
        val hardRoutes = easyRoutes
        var notFinished = true
        var deadEnd = false
        while(notFinished && score <= currentFastest) {
            if (current == Pair(4, 2)) {
                val z = 1
                z
            }
            val moves = moves(current).filterNot{ soFar.contains(it) }.sortedBy { toTarget(it) }
            when {
                moves.isEmpty() -> {
                    deadEnd = true
                    notFinished = false
                }
                moves.contains(target) -> {
                    score += (if (tool == 't') 1 else 8)
                    notFinished = false
                    if (score < currentFastest) {
                        currentFastest = score
                    }
                }
                else -> {
                    soFar.add(current)
                    val goodMatches = goodMatch(tool)
                    val easyMoves = moves.filter { goodMatches.contains(cellScore(it)) }
                    when {
                        easyMoves.isNotEmpty() -> {
                            println("Easy move from $current to ${easyMoves.first()} with ${this.tool} also ${easyMoves.size - 1}")
                            current = easyMoves.first()
                            score += 1
                            easyRoutes.addAll(easyMoves.drop(1).map { move ->
                                copy(current = move)
                            })
                        }
                        else -> {
                            this.current = moves.first()
                            this.score += 8
                            val toolsFor1 = toolsForMove(moves.first())
                            this.tool = toolsFor1.first()
                            hardRoutes.add(copy(tool = toolsFor1.last()))
                            hardRoutes.addAll(moves.drop(1).flatMap {move ->
                                toolsForMove(move).map {tool ->
                                    copy(current = move, tool = tool)
                                }
                            })

                        }
                    }
                }
            }
        }
        if (!deadEnd && score <= currentFastest) {
            println("Good route $this")
        }
        val routeOptions = mutableListOf<Route>()
        routeOptions.add(if (deadEnd) deadRoute else this)
        routeOptions.addAll(easyRoutes.reversed().map { it.route()?: deadRoute})
        return routeOptions.sortedBy { it.score }.elementAtOrElse(0) {deadRoute}
    }
}

val deadRoute = Route(Pair(0, 0), Int.MAX_VALUE)

fun geoIndex(point: Pair<Int, Int>): Int =
    when {
        point == Pair(0, 0) -> 0
        point == target -> 0
        point.second == 0 -> point.first * 16807
        point.first == 0 -> point.second * 48271
        else -> eroLevel(point.copy(first = point.first - 1)) * eroLevel(point.copy(second = point.second - 1))
    }

val eroLvlMemo = mutableMapOf<Pair<Int, Int>, Int>()

fun eroLevel(point: Pair<Int, Int>): Int =
    eroLvlMemo.getOrPut(point) {
        val geo = geoIndex(point)
        val dep = geo + depth
        val modBig = dep % 20183
        val modSmall = modBig % 3
        modBig
    }

fun cellScore(point: Pair<Int, Int>) =
        eroLevel(point) % 3

fun printC(cellScore: Int) =
        when(cellScore % 3) {
          0 -> '.'
          1 -> '='
          2 -> '|'
          else -> '?'
        }

fun main(vararg args: String) {
    (0..target.first + 8).forEach { x -> eroLevel(Pair(x, 0)) }
    (0..target.second + 8).forEach { y -> eroLevel(Pair(0, y)) }
    (1..target.second + 8).forEach { y ->
        (1..target.first + 8).forEach { x ->
            eroLevel(Pair(x, y))
        }
    }
    var riskLevel = 0
    (0..target.second + 8).forEach { y ->
        (0..target.first + 8).forEach { x ->
            val point = Pair(x, y)
            val cellScore = cellScore(point)
            riskLevel += cellScore
            print(if (point == target) 'T' else printC(cellScore))
        }
        println()
    }
    println("Risk level $riskLevel")
    val result = Route().route()
    println("Result ${result?.score?:-1}")
}