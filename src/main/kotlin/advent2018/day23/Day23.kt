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

    val interSections = mutableMapOf<Nanobot, List<Nanobot>>()
    nanos.forEach { candidate -> interSections[candidate] = nanos.filter{ test -> candidate.intersects(test) } }
    val inters = interSections.entries.sortedByDescending { (k, v) -> v.size}
    println("Most intersections ${inters.first().key} with ${inters.first().value.size}")
}