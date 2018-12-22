package advent2018.day22

import kotlin.math.absoluteValue

val target = Pair(7,770)//Pair(10, 10)//
const val depth = 10647//510//
var currentFastest = 1067
const val padFactor = 50


val bestPointScores = mutableMapOf<Pair<Pair<Int,Int>, Char>, Int>()

fun moves(point: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    result.add(point.copy(first = point.first - 1))
    result.add(point.copy(first = point.first + 1))
    result.add(point.copy(second = point.second - 1))
    result.add(point.copy(second = point.second + 1))

    return result.filter { (x, y) -> x >= 0 && y >= 0 && x <= target.first + 100 && y <= target.second + 20}
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

val liveRoutes = mutableSetOf(Route())
val completeRoutes = mutableListOf<Route>()

data class Route(
    var current: Pair<Int, Int> = Pair(0, 0),
    var score: Int = 0,
    var tool: Char = 't',
    val soFar: MutableSet<Pair<Int, Int>> = mutableSetOf()) {

    fun route(): Route {
        var notFinished = true
        var hitTarget = false
        while(notFinished && score <= currentFastest) {
            if (current == Pair(4, 1) || current == Pair(4, 2)) {
                val z = 1
                z
            }
            val moves = moves(current).filterNot{ soFar.contains(it) }.sortedBy { toTarget(it) }
            when {
                moves.isEmpty() -> {
                    notFinished = false
                }
                moves.contains(target) -> {
                    score += (if (tool == 't') 1 else 8)
                    notFinished = false
                    hitTarget = true
                    if (score < currentFastest) {
                        currentFastest = score
                        completeRoutes.add(this)
                    }
                }
                else -> {
                    val myScoreKey = Pair(current, tool)
                    val bestPointScore = bestPointScores.getOrDefault(myScoreKey, Int.MAX_VALUE)
                    if (score < bestPointScore) {
                        bestPointScores[myScoreKey] =  score
                        soFar.add(current)
                        val goodMatches = goodMatch(tool)
                        val easyMoves = moves.filter { goodMatches.contains(cellScore(it)) }
                        val easyScore = score + 1
                        val hardScore = score + 8
                        if (easyMoves.isNotEmpty()) {
                            current = easyMoves.first()
                            score = easyScore
                            liveRoutes.addAll(easyMoves.drop(1).map { move ->
                                copy(current = move, soFar = cloneRoute())
                            })
                        } else {
                            notFinished = false
                        }
                        var harderMoves = moves.filterNot {
                            easyMoves.contains(it)
                        }
                        if (harderMoves.isNotEmpty()) {
                            val toolsForCurrent = toolsForMove(current)
                            val toolsForMove = toolsForMove(harderMoves.first())
                            val tool = toolsForMove.find { toolsForCurrent.contains(it) }?:'?'
                            if (easyMoves.isEmpty()) {
                                current = harderMoves.first()
                                score = hardScore
                                liveRoutes.add(this)
                                liveRoutes.add(copy(tool = tool, soFar = cloneRoute()))
                                harderMoves = harderMoves.drop(1)
                            }
                            liveRoutes.addAll(harderMoves.map { move ->
                                copy(current = move, tool = tool, score = hardScore, soFar = cloneRoute())
                            })
                        }
                    }
                    else {
                        notFinished = false
                    }
                }
            }
        }

        return if (hitTarget) {
            println("Good route $this")
            this
        }
        else {
            deadRoute
        }
    }

    private fun cloneRoute(): MutableSet<Pair<Int, Int>> {
        val newRoute = mutableSetOf<Pair<Int, Int>>()
        newRoute.addAll(soFar)
        return newRoute
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

fun mergeRoutes() {
    liveRoutes.groupBy { Pair(it.current, it.tool) }.filterValues { it.size > 1 }.forEach { (point, routes) ->
        liveRoutes.removeAll(routes)
        val superSoFar = mutableSetOf<Pair<Int, Int>>()
        routes.forEach { superSoFar.addAll(it.soFar) }
        val superScore = routes.sortedBy { it.score }.first()
        liveRoutes.add(superScore.copy(soFar = superSoFar))
    }

}

fun main(vararg args: String) {

    (0..target.first + 100).forEach { x -> eroLevel(Pair(x, 0)) }
    (0..target.second + 20).forEach { y -> eroLevel(Pair(0, y)) }
    (1..target.second + 20).forEach { y ->
        (1..target.first + 100).forEach { x ->
            eroLevel(Pair(x, y))
        }
    }
    var riskLevel = 0
    (0..target.second + 20).forEach { y ->
        (0..target.first + 100).forEach { x ->
            val point = Pair(x, y)
            val cellScore = cellScore(point)
            val route = winner.soFar.contains(point)
            if (y <= target.second && x <= target.first) riskLevel += cellScore
            print(if (point == target) 'T' else if (route) '~' else printC(cellScore))
        }
        println()
    }
    println("Risk level $riskLevel")
}