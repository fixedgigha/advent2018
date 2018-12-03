package advent2018

import java.io.File

fun main(args: Array<String>)  {
    val lines = File("src/main/resources/day3/input.txt").readLines()
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
            listOf(c)
        }
        else {
            emptyList()
        }
    }
    val count = claims.first<Claim> {
        var found = false
        for (x in it.left .. (it.left + it.width - 1))
            for (y in it.top .. (it.top + it.height - 1))
                found = found ||  (claims.filter {c -> c.contains(x, y)} .take(2).size == 2)
        !found
    }.id
    println("$count")
}