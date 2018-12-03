package advent2018.day8

import java.io.File

fun process2(input: MutableList<Int>): Int {
    val childCount = input.removeAt(0)
    val metaCount = input.removeAt(0)
    val childScores = (1 .. childCount).map { process2(input) }

    return (1..metaCount).fold(0) {total, _ ->
        val meta = input.removeAt(0)
        total +
            if (childCount == 0) {
                meta
            }
            else {
                if (meta > 0 && meta <= childScores.size)
                    childScores[meta - 1]
                else
                    0
            }
    }
}

fun main(args: Array<String>) {
    val input = mutableListOf<Int>()
    input.addAll(
        File("src/main/resources/day8/input.txt").readText().split(" ").map { it.toInt() }
    )
    println(process2(input))

}