package advent2018.day18

import java.io.File


var data = mutableListOf<CharArray>()

fun loadData(fileName: String) {
    File("src/main/resources/day18/$fileName").readLines().forEach { line ->
        val ca = CharArray(line.length + 2)
        ca[0] = '-'
        line.toCharArray().copyInto(ca,1)
        ca[line.length+1] = '-'

        data.add(ca)
    }
    val headFoot = CharArray(data.first().size)
    headFoot.fill('-')
    data.add(0, headFoot)
    data.add(headFoot)
}

fun process(char: Char, x: Int, y: Int): Char {
    val around = listOf(
        data[y+1][x-1],
        data[y+1][x],
        data[y+1][x+1],
        data[y][x+1],
        data[y][x-1],
        data[y-1][x-1],
        data[y-1][x],
        data[y-1][x+1]
    )
    return when {
        char == '.' && around.filter { it == '|' }.size >= 3 -> '|'
        char == '|' && around.filter { it == '#' }.size >= 3 -> '#'
        char == '#' && around.filter { it == '|' || it == '#' }.groupBy{ it }.size < 2 -> '.'
        else -> char
    }
}

fun transform() {
    val newData = mutableListOf<CharArray>()
    data.forEachIndexed { y, chars ->
        val newChars = CharArray(chars.size)
        chars.forEachIndexed { x, char ->
            newChars[x] = when {
                char == '-' -> '-'
                else -> process(char, x, y)
            }
        }
        newData.add(newChars)
    }
    data = newData
}

fun printData() {
    data.forEachIndexed { y, chars ->
        chars.forEachIndexed{ x, char ->
            print(char)
        }
        println()
    }
}

fun main(vararg args: String) {
    loadData("input.txt")
    printData()
    (1..10).forEach {round->
        println("Round $round")
        transform()
        printData()
    }
    val treeCount = data.fold(0) { total, chars ->
        total + chars.filter{ it == '|' }.size
    }
    val lumberCount = data.fold(0) { total, chars ->
        total + chars.filter{ it == '#' }.size
    }
    println("Final score ${treeCount * lumberCount}")
}