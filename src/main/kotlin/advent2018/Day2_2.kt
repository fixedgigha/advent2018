package advent2018

import java.io.File


fun disjoint(str1: String, str2: String) : String {
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

    val results = ArrayList<String>()
    lines.forEach {line1 ->
        lines.forEach {line2 ->
            results.add(disjoint(line1, line2))
        }
    }
    results.sortByDescending {s -> s.length}
    println(results[0])
}
