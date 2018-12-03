package advent2018

import java.io.File


fun intersection(str1: String, str2: String) : String {
    var result = ""
    if (str1 != str2) {
        str1.forEachIndexed { index, c ->
            if (c == str2[index]) {
                result += c
            }
        }
    }
    return result
}

fun main(args : Array<String>) : Unit {
    val lines = File("src/main/resources/day2/input.txt").readLines()

    val longest =
    lines.fold("")  { current, line1 ->
        lines.fold(current)  { currnt, line2 ->
            val inter = intersection(line1, line2)
            if (inter.length > currnt.length)  inter else currnt
        }
    }
    println(longest)
    println(longest == "oeylbtcxjqnzhgyylfapviusr")
}
