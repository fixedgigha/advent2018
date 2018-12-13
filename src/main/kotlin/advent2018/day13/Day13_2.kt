package advent2018.day13


fun takeTurn2() : Pair<Boolean, Pair<Int, Int>?> {
    cars.sort()
    cars.forEach {mover ->
        if (mover.alive) {
            val currentTrack = grid[mover.y][mover.x]
            when (currentTrack) {
                '|', '-' -> straighTrack(mover)
                '/' -> forwardSlash(mover)
                '\\' -> backwardSlash(mover)
                '+' -> intersection(mover)
            }

            if (cars.filter { it.alive && it.x == mover.x && it.y == mover.y }.size == 2) {
                println("Crash at ${cars.filter { it.x == mover.x && it.y == mover.y }}")
                cars.forEach { if (it.x == mover.x && it.y == mover.y) it.alive = false }
            }
        }
    }
    val living = cars.filter { it.alive }
    if (living.size  == 1)
        return Pair(true, Pair(living[0].x, living[0].y))

    return Pair(false, null)
}

fun main(vararg args: String) {
    grid.forEach { row ->
        println(row.joinToString(""))
    }
    println(cars)
    while (true) {
        val turnResult = takeTurn2()
        if (turnResult.first) {
            println("Surviving at ${turnResult.second}")
            break
        }
        println(cars.filter {it.alive})
    }
}