package advent2018.day9

import java.util.*


fun main(args: Array<String>) {

    val marbles = LinkedList(listOf(0, 1))
    val scores = mutableMapOf<Int, Int>()

    var currentPlayer = 2
    val totalPlayers = 486
    var lastInsertIndex = 1

    fun scoringIndex(): Int =
        if (lastInsertIndex - 7 >= 0) {
            lastInsertIndex - 7
        }
        else {
            marbles.lastIndex - (6 - lastInsertIndex)
        }

    for (m in 2..7083300) {
        if (m % 10000 == 0) {
            println(">> $m")
        }
        val newInsertIndex =
        if (m % 23 == 0) {
            val scoringIndex = scoringIndex()
            scores[currentPlayer] =  scores.getOrPut(currentPlayer) { -> 0} + m + marbles.removeAt(scoringIndex)
            scoringIndex
        }
        else {
            val nii =
                if (lastInsertIndex == marbles.lastIndex) 1
                else lastInsertIndex + 2
            marbles.add(nii, m)
            nii
        }

        lastInsertIndex = newInsertIndex
        currentPlayer = if (currentPlayer < totalPlayers) currentPlayer + 1 else 1
    }

    println(scores.entries.sortedByDescending { it.value }.first())

}