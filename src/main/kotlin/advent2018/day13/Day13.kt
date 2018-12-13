package advent2018.day13

import java.io.File

var maxX = 0

val grid: MutableList<MutableList<Char>> = {
    File("src/main/resources/day13/testInput.txt").readLines().map {line ->
        val ca = line.toCharArray()
        maxX = Math.max(maxX, ca.size)
        ca.toMutableList()
    }.toMutableList()
}()

data class Car(var x: Int, var y: Int, var dir: Char, var nextInter: Char = 'l' ) : Comparable<Car> {
    override fun compareTo(other: Car): Int {
        return if (y < other.y || (y == other.y && x < other.x)) -1
        else if (y == other.y && x == other.x) 0
        else 1
    }
}

val cars: MutableList<Car> = {
    val result = mutableListOf<Car>()
    grid.forEachIndexed {y, row ->
       row.forEachIndexed { x, c ->
           val segment = grid[y][x]
           if (listOf('<', '^', 'v', '>').contains(segment)) {
               grid[y][x] = if (segment == '<' || segment == '>') '-' else '|'
               result.add(Car(x, y, segment))
           }
       }
    }
    result
}()

fun straighTrack(it: Car) {
    when (it.dir) {
        '>' -> it.x = it.x + 1
        '<' -> it.x = it.x - 1
        '^' -> it.y = it.y - 1
        else -> it.y = it.y + 1
    }
}

fun forwardSlash(it: Car) {
    when (it.dir) {
        '<' -> {
            it.y = it.y + 1
            it.dir = 'v'
        }
        '^' -> {
            it.x = it.x + 1
            it.dir = '>'
        }
        '>' -> {
            it.y = it.y - 1
            it.dir = '^'
        }
        'v' -> {
            it.x = it.x - 1
            it.dir = '<'
        }
    }

}

fun backwardSlash(it: Car) {
    when (it.dir) {
        '>' -> {
            it.y = it.y + 1
            it.dir = 'v'
        }
        'v' -> {
            it.x = it.x + 1
            it.dir = '>'
        }
        '<' -> {
            it.y = it.y - 1
            it.dir = '^'
        }
        '^' -> {
            it.x = it.x - 1
            it.dir = '<'
        }
    }
}

fun intersection(it: Car) {
    when(it.nextInter) {
        'l'
    }
}


fun takeTurn() : Pair<Boolean, Pair<Int, Int>?> {
    cars.sort()
    cars.forEach {
        val currentTrack = grid[it.y][it.x]
        when (currentTrack) {
            '|', '-' -> straighTrack(it)
            '/' -> forwardSlash(it)
            '\\' -> backwardSlash(it)
            '+' -> intersection(it)
        }
    }
    return Pair(true, Pair(6, 6))
}

fun main(vararg args: String) {
    grid.forEach { row ->
        println(row.joinToString(""))
    }
    println(cars)
    while (true) {
        val turnResult = takeTurn()
        if (turnResult.first) {
            println("Crashed at ${turnResult.second}")
            break
        }
    }
}