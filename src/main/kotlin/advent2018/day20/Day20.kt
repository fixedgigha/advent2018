package advent2018.day20
import java.io.File

val text = File("src/main/resources/day20/input.txt").readText()
var current = 1

data class Node(val startScore: Int = 0, var count: Int = 0, var subNodes: MutableList<Node>  = mutableListOf()) {
    fun total() = startScore + count

    fun scoreMil(): Int =
            if (startScore < 1000 && startScore + count > 1000)
                (startScore + count) - 1000
            else if (startScore > 1000)
                count
            else
                0
}

fun loadNodes(startScore: Int, endChar: Char = ')'): MutableList<Node> {
    val branchScores = mutableListOf(Node(startScore))
    var currentBranch = 0
    var finished = false
    while (!finished) {

        when(text[current]) {
            endChar -> {
                current++
                finished = true
             }
            '|' -> {
                current++
                currentBranch++
                branchScores.add(Node(startScore))
            }
            '(' -> {
                current++
                val subNodes = loadNodes(branchScores[currentBranch].total())
                if (subNodes.last().count == 0) {
                    // out and bck scenario
                    if (subNodes.size != 2) {
                        println("WTF")
                    }
                    subNodes.first().count /= 2
                    subNodes.removeAt(subNodes.lastIndex)
                }
                branchScores[currentBranch].subNodes.addAll(subNodes)
            }
            else -> {
                branchScores[currentBranch].count += 1
                current++
            }
        }
    }
    return branchScores
}

fun part1Total(node: Node): Int =
    node.subNodes.map { part1Total(it) }.max()?:node.total()

fun part2Total(node: Node): Int =
    node.scoreMil() + node.subNodes.map { part2Total(it) }.sum()


fun main(vararg args: String) {
    val rootNodes = loadNodes(0, '$')

    val part1Total = part1Total(rootNodes.first())

    println("Part 1 $part1Total")

    val part2Total = part2Total(rootNodes.first())

    println("Part 2 $part2Total")
}