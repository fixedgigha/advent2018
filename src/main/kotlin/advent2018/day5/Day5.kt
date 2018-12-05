package advent2018.day5

import java.io.File

fun swapCase(c : Char): Char = if (c.isUpperCase()) c.toLowerCase() else c.toUpperCase()

val NUL = 0.toChar()

fun react(input : String, remove: Char): Int {
    var text = input.toCharArray()
    var length = 0
    while (length == 0) {
        for (x in 0..(text.size - 2)) {
            if (text[x].equals(remove, true)) {
                text[x] = NUL
            }
            else if (text[x] == swapCase(text[x + 1])) {
                text[x] = NUL
                text[x + 1] = NUL
            }
        }
        val newText = text.filter { it != NUL }.fold(StringBuilder()) { sb, c ->
            sb.append(c)
        }.toString()
        if (text.size == newText.length) {
            length = newText.length
        }
        else {
            text = newText.toCharArray()
        }
    }
    return length
}

fun main(args: Array<String>) {
    var text = File("src/main/resources/day5/input.txt").readText()
    val analysis = mutableMapOf<Char, Int>()
    for (a in 'a' .. 'z') {
        val score = react(text, a)
        analysis[a] = score
    }
    println(analysis.entries.sortedBy { it.value }.first())
}