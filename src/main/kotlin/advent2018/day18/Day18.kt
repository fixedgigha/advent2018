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

fun score(): Long {
    val treeCount = data.fold(0) { total, chars ->
        total + chars.filter{ it == '|' }.size
    }
    val lumberCount = data.fold(0) { total, chars ->
        total + chars.filter{ it == '#' }.size
    }
    return treeCount.toLong() * lumberCount
}

fun printData() {
    data.forEach { chars ->
        chars.forEach { char ->
            print(char)
        }
        println()
    }
}

fun main(vararg args: String) {
    loadData("input.txt")
    val deltas = mutableListOf(0L)
    //printData()
    val analysis = (1..10000).map {_->
            transform()
            //printData()
            val score = score()
            deltas.add(score - deltas.last())
            score
        }.mapIndexed { index, i ->  Pair(i, index)}.groupBy { it.first }.entries.sortedByDescending {(k, v) -> v.size  }


    println(analysis.take(20).map { Pair(it.key, it.value.size)})
}