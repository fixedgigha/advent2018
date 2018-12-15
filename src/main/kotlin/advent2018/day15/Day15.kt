package advent2018.day15

import java.io.File
import kotlin.math.absoluteValue

data class Dude(val type: Char, var x: Int, var y: Int, var health: Int = 200): Comparable<Dude> {

    fun alive() = health > 0

    override fun compareTo(other: Dude): Int {
        return if (y < other.y || (y == other.y && x < other.x)) -1
        else if (y == other.y && x == other.x) 0
        else 1
    }

    val killPower = if (type == 'E') 34 else 3
}

class ComparablePoint(val point: Pair<Int, Int>) : Comparable<ComparablePoint> {
    override fun compareTo(other: ComparablePoint): Int =
        when {
            point.second < other.point.second -> -1
            point.second == other.point.second && point.first < other.point.first -> -1
            point.second == other.point.second && point.first == other.point.first -> 0
            else -> 1
        }
}

class Game(fileName: String) {

    private val map: List<CharArray>
    private val dudes: List<Dude>
    init {
        val (map1, dudes1) = loadBoard(fileName)
        map = map1
        dudes = dudes1
    }

    private fun loadBoard(fileName: String): Pair<List<CharArray>, List<Dude>> {
        val map = mutableListOf<CharArray>()
        val dudes = mutableListOf<Dude>()
        File("src/main/resources/day15/$fileName").readLines().forEachIndexed { y, line ->
            val row = CharArray(line.length)
            map.add(row)
            line.forEachIndexed { x, ch ->
                when (ch) {
                    '#', '.' -> row[x] = ch
                    'G', 'E' -> {
                        row[x] = '.'
                        dudes.add(Dude(ch, x, y))
                    }
                }
            }
        }
        return Pair(map, dudes)
    }

    private fun attackableTargets(dude: Dude, targets: List<Dude>): List<Dude> {
        return targets.filter {
            ((it.x - dude.x).absoluteValue == 1 && it.y == dude.y) ||
            ((it.y - dude.y).absoluteValue == 1 && it.x == dude.x)
        }
    }

    private fun coordDude(x: Int, y: Int) =
        dudes.find { it.alive() && it.x == x && it.y == y }

    private fun coordIsDude(x: Int, y: Int) =
        dudes.find { it.alive() && it.x == x && it.y == y } != null

    private fun coordIsSpace(x: Int, y: Int) =
        map[y][x] == '.'

    private fun openSpacesAround(x: Int, y: Int) =
        listOf(
            Pair(x - 1, y),
            Pair(x + 1, y),
            Pair(x, y - 1),
            Pair(x, y + 1)
        ).filter { (x, y) ->
            coordIsSpace(x, y) && !coordIsDude(x, y)
        }


    private fun inRange(targets: List<Dude>) =
        targets.flatMap {
            openSpacesAround(it.x, it.y)
        }


    private fun routesToTarget(
        x: Int,
        y: Int,
        target: Pair<Int, Int>,
        shortestSoFar: Int
    ): Pair<Int, Pair<Int, Int>?> {
        class Candidate(val startFrom: Pair<Int, Int>, val current: Pair<Int,Int>, val considered: MutableSet<Pair<Int, Int>>)
        var candidates = openSpacesAround(x, y).sortedBy { absolute(it, target) }.map { space ->
            Candidate(space, space, mutableSetOf(Pair(x, y), space))
        }

        val winners = mutableListOf<Candidate>()
        winners.addAll(candidates.filter{ it.current == target})
        var steps = 1
        while (winners.isEmpty() && candidates.isNotEmpty() && steps < shortestSoFar) {
            val nextCandidates = mutableListOf<Candidate>()

            candidates.sortedBy { absolute(it.current, target) }.forEach {candidate ->
                openSpacesAround(candidate.current.first, candidate.current.second)
                    .filter { !candidate.considered.contains(it) }
                    .sortedBy { absolute(it, target)}
                    .forEach {newPoint ->
                        val consideredPoints = mutableSetOf<Pair<Int, Int>>()
                        consideredPoints.addAll(candidate.considered)
                        consideredPoints.add(newPoint)
                        val newCandidate = Candidate(candidate.startFrom, newPoint, consideredPoints)
                        if (newPoint == target) {
                            winners.add(newCandidate)
                        }
                        else if (winners.isEmpty()) {
                            if (steps > 3) {
                                val mergeCandidate = nextCandidates.find { it.startFrom == newCandidate.startFrom && it.current == newCandidate.current }
                                if (mergeCandidate != null) {
                                    mergeCandidate.considered.addAll(newCandidate.considered)
                                }
                                else {
                                    nextCandidates.add(newCandidate)
                                }
                            }
                            else {
                                nextCandidates.add(newCandidate)
                            }
                        }
                    }
            }
            steps++
            candidates = nextCandidates
        }
        return if (winners.isNotEmpty()) {
            Pair(steps, winners.map { it.startFrom }.sortedBy { ComparablePoint(it) }.first())
        }
        else {
            Pair(-1, null)
        }
    }

    private fun absolute(x: Int, y: Int, point: Pair<Int, Int>) = (x - point.first).absoluteValue + (y - point.second).absoluteValue
    private fun absolute(point1: Pair<Int,Int>, point2: Pair<Int, Int>) = (point1.first - point2.first).absoluteValue + (point1.second - point2.second).absoluteValue

    private fun takeARound(dude: Dude, dudes: List<Dude>): Boolean {
        if (!dude.alive()) return false
        val targets = dudes.filter { it.alive() && it.type != dude.type }
        if (targets.isEmpty())
            return true // game over
        var attackable = attackableTargets(dude, targets)
        if (attackable.isEmpty()) {
            // workout next move
            val inRange = inRange(targets).sortedBy {absolute(dude.x, dude.y, it) }
            var shortestSoFar = Int.MAX_VALUE
            val routesToTarget = inRange
                .flatMap {target ->
                    if (absolute(dude.x, dude.y, target) > shortestSoFar) {
                        emptyList()
                    }
                    else {
                        val routes = routesToTarget(dude.x, dude.y, target, shortestSoFar)
                        if (routes.second != null) {
                            if (routes.first < shortestSoFar) shortestSoFar = routes.first
                            listOf(routes)
                        }
                        else {
                            emptyList()
                        }
                    }
                }
                .sortedBy { it.first }
            if (routesToTarget.isNotEmpty()) {
                val fewestSteps = routesToTarget.first().first
                val nextMove = routesToTarget.takeWhile { it.first == fewestSteps}.sortedBy{ ComparablePoint(it.second?:Pair(Int.MAX_VALUE, Int.MAX_VALUE)) }. first()
                // move
                dude.x = nextMove.second?.first?:dude.x
                dude.y = nextMove.second?.second?:dude.y
                attackable = attackableTargets(dude, targets)
            }
        }
        if(attackable.isNotEmpty()) {
            val lowestHealth = attackable.sortedBy { it.health }.first().health
            val victim = attackable
                .sortedBy { it.health }
                .takeWhile { it.health == lowestHealth }
                .sortedBy { ComparablePoint(Pair(it.x, it.y)) }
                .first()
            victim.health -= dude.killPower
        }
        return false
    }

    fun round(): Boolean {
        val liveDudes = liveDudes()
        return liveDudes.fold(false) {outcome, dude ->
                outcome || takeARound(dude, liveDudes)
        }
    }

    fun liveDudes() = dudes.filter{ it.health > 0 }.sorted()

    fun deadElves() = dudes.filter{ it.health <= 0 && it.type == 'E' }

    fun drawBoard() {
        map.forEachIndexed {y, row ->
            row.forEachIndexed {x, cell ->
                print(coordDude(x, y)?.type?:cell)
            }
            println()
        }
        println(liveDudes())
    }
}

fun main(vararg args: String) {
    val game = Game("input.txt")
    var round = 0
    while (true) {
        if (game.round()) break
        println("After round $round")
        game.drawBoard()
        round++
    }
    println(round)
    println(game.liveDudes().map { it.health })
    val result = game.liveDudes().map { it.health }.sum() * round
    println("Final result is $result winners are ${game.liveDudes().first().type} dead elves ${game.deadElves().size}")
}