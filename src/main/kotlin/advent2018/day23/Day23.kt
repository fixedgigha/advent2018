package advent2018.day23

import advent2018.day13.intersection
import java.io.File
import kotlin.math.absoluteValue

data class Nanobot(val pos: Triple<Int, Int, Int>, val radius: Int) {

    fun distance(nano: Nanobot): Int =
        (pos.first - nano.pos.first).absoluteValue +
                (pos.second - nano.pos.second).absoluteValue +
                (pos.third - nano.pos.third).absoluteValue

    fun inRange(nano: Nanobot): Boolean =
            distance(nano) <= radius

    fun intersects(nano: Nanobot): Boolean =
            distance(nano) <= radius + nano.radius


}

val regex = Regex("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)")

val nonBot = Nanobot(Triple(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE), Int.MIN_VALUE)

fun absPoints(points: Triple<Int, Int, Int>) = points.first.absoluteValue + points.second.absoluteValue + points.third.absoluteValue

fun main(vararg args: String) {
    val nanos = File("src/main/resources/day23/input.txt").readLines().map {line ->
        regex.matchEntire(line)?.let {mr ->
            Nanobot(Triple(mr.groupValues[1].toInt(), mr.groupValues[2].toInt(), mr.groupValues[3].toInt()), mr.groupValues[4].toInt())
        }?: nonBot
    }.filterNot {  it == nonBot }
    val focal = nanos.sortedByDescending { it.radius }.first()
    println("Max radius ${focal.radius}")
    val result = nanos.filter(focal::inRange).count()
    println("Part 1 result $result")

    var minX = nanos.fold(Int.MAX_VALUE) {current, nano-> Math.min(current, nano.pos.first) }
    var maxX = nanos.fold(Int.MIN_VALUE) {current, nano-> Math.max(current, nano.pos.first) }
    var minY = nanos.fold(Int.MAX_VALUE) {current, nano-> Math.min(current, nano.pos.second) }
    var maxY = nanos.fold(Int.MIN_VALUE) {current, nano-> Math.max(current, nano.pos.second) }
    var minZ = nanos.fold(Int.MAX_VALUE) {current, nano-> Math.min(current, nano.pos.third) }
    var maxZ = nanos.fold(Int.MIN_VALUE) {current, nano-> Math.max(current, nano.pos.third) }

    var search = 1
    while (search < (maxX - minX)) search *= 2
    var found: Nanobot? = null
    while (found == null) {
        var best = Pair(Triple(0, 0, 0), 0)
        (minZ..maxZ + 1 step(search)).forEach {z ->
            (minY..maxY + 1 step(search)).forEach { y ->
                (minX..maxX + 1 step (search)).forEach { x ->
                    val test = Nanobot(pos = Triple(x, y, z), radius = 0)
                    val score = nanos.filter { nano -> (nano.distance(test) - nano.radius) <= search }.count()
                    if (score > best.second || (score == best.second && absPoints(test.pos) < absPoints(best.first))) {
                        best = Pair(test.pos, score)
                    }
                }
            }
        }
        if (search == 1) {
            found = Nanobot(pos = best.first, radius = 0)
        }
        else {
            minX = best.first.first - search
            maxX = best.first.first + search
            minY = best.first.second - search
            maxY = best.first.second + search
            minZ = best.first.third - search
            maxZ = best.first.third + search
            search /= 2
        }
    }
    println("Result $found")
    println(if (found != null) absPoints(found.pos) else 0)
}