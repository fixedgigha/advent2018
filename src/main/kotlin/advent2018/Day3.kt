package advent2018

import java.io.File

data class Claim(val id : Int,
                 val left : Int,
                 val top : Int,
                 val width : Int,
                 val height : Int) {
    fun contains(x: Int, y: Int) : Boolean =
        (x >= left) && (x < (left + width)) &&
                (y >= top) && (y < (top + height))

}

val inputMatch = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+).*")
fun matchToInt(mr: MatchResult, x: Int) = mr.groupValues[x].toInt()

fun main(args: Array<String>)  {
    val lines = File("src/main/resources/day3/input.txt").readLines()
    var maxX = 0
    var maxY = 0
    val claims = lines.flatMap {l ->
        val mr = inputMatch.matchEntire(l)
        if (mr != null) {
            val c = Claim(
                matchToInt(mr, 1),
                matchToInt(mr, 2),
                matchToInt(mr, 3),
                matchToInt(mr, 4),
                matchToInt(mr, 5)
            )
            if (maxX < c.left + c.width)
                maxX = c.left + c.width
            if (maxY < c.top + c.height)
                maxY = c.top + c.height
            listOf(c)
        }
        else {
            emptyList()
        }
    }
    var count = 0
    for (x in 0 .. (maxX + 1)) for (y in 0 .. (maxY + 1)) {
        if (claims.filter {it.contains(x, y)}.take(2).size == 2) {
            count++
        }
    }
    println("$count")
}