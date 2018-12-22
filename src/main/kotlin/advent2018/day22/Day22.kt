package advent2018.day22

val target = Pair(7,770)//Pair(10, 10)//
const val depth = 10647//510//

data class Node(val point: Pair<Int, Int> = Pair(0, 0), val tool: Char = 't', val score: Int = 0)

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

fun toolsForMove(point: Pair<Int, Int>) =
    when(cellScore(point)) {
        0 -> listOf('c', 't')
        1 -> listOf( 'n', 'c')
        else -> listOf('t', 'n')
    }


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
            if (y <= target.second && x <= target.first) riskLevel += cellScore
        }
    }
    println("Risk level $riskLevel")
    val bestPointScores = mutableMapOf<Pair<Pair<Int,Int>, Char>, Int>()
    var liveNodes = mutableListOf(Node())
    while (liveNodes.isNotEmpty()) {
        val newNodes = mutableListOf<Node>()
        liveNodes.forEach {live ->
            val goodMoves = goodMatch(live.tool)
            moves(live.point).forEach { move ->
                val type = cellScore(move)
                val newNode =
                    if (goodMoves.contains(type)) {
                        live.copy(point = move, score = live.score + 1)
                    }
                    else {
                        val currentTools = toolsForMove(live.point)
                        live.copy(point = move, score = live.score + 8, tool = toolsForMove(move).first { currentTools.contains(it) })
                    }
                val scoreKey = Pair(newNode.point, newNode.tool)
                val currentScore = bestPointScores.getOrDefault(scoreKey, newNode.score + 1)
                if (newNode.score < currentScore) {
                    newNodes.add(newNode)
                    bestPointScores[scoreKey] = newNode.score
                }
            }


        }
        liveNodes = newNodes
    }
    println("torch=${bestPointScores[Pair(target, 't')]}")

}