package advent2018.day11

const val serial = 2866

fun cellScore(x: Int, y: Int): Int {
    val rackID = x + 10
    val powerLvl = rackID * y
    val total = (powerLvl + serial) * rackID
    val totalStr = total.toString()
    val hundred = totalStr[totalStr.length - 3].toString().toInt()
    return hundred - 5
}

val cellScores = mutableMapOf<Triple<Int, Int, Int>, Int>()

fun gridScore(x: Int, y: Int, size: Int): Int {
    val score =
        if (size == 1) {
            cellScore(x, y)
        }
        else {
            (cellScores.remove(Triple(x, y, size - 1)) ?: Int.MIN_VALUE) +
            (0..(size - 1)).map {a -> cellScore(x + size - 1, y + a) +
                                      cellScore(x + a, y + size - 1) }.sum()
        }
    if (x < (301 - size) && y < (301 - size))
        cellScores[Triple(x, y, size)] =  score
    return score
}

fun main(args: Array<String>) {
    println(cellScore(3, 5))
    println(gridScore(3, 5, 1))
    var currentHighest = Int.MIN_VALUE
    var currentXY = Triple(0, 0, 0)
    for (size in 1 .. 300) {
        println(">>> $size $currentXY $currentHighest")
        for (x in 1..(301 - size))
            for (y in 1..(301 - size)) {
                val gridScore = gridScore(x, y, size)
                if (gridScore > currentHighest) {
                    currentHighest = gridScore
                    currentXY = Triple(x, y, size)
                }
            }
    }
    println(currentXY)
    println(currentHighest)
}