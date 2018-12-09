package advent2018.day9

data class Node(val value: Int, var prev: Node? = null, var next: Node? = null) {
    override fun toString() = "$value: ${prev?.value} <> ${next?.value}"
}

val NODE_OF_DOOM = Node(-1)

fun main(args: Array<String>) {

    var head = Node(0)
    var last = Node(1, head, head)
    head.next = last
    head.prev = last

    val scores = mutableMapOf<Int, Long>()

    var currentPlayer = 2
    val totalPlayers = 486
    var lastInsertNode = last

    fun scoringNode(): Node {
        var scoringNode = lastInsertNode
        for (n in 0..6) {
            scoringNode = scoringNode.prev?: NODE_OF_DOOM
        }
        return scoringNode
    }

    for (m in 2..7083300) {
        if (m % 23 == 0) {
            val scoringNode = scoringNode()
            scores[currentPlayer] =  scores.getOrPut(currentPlayer) { -> 0} + m + scoringNode.value
            scoringNode.prev?.next = scoringNode.next
            scoringNode.next?.prev = scoringNode.prev
            lastInsertNode = scoringNode.next?: NODE_OF_DOOM
        }
        else {
            val insertAfter = lastInsertNode.next
            lastInsertNode = Node(m, insertAfter, insertAfter?.next)
            insertAfter?.next?.prev = lastInsertNode
            insertAfter?.next = lastInsertNode
        }

        currentPlayer = if (currentPlayer < totalPlayers) currentPlayer + 1 else 1
    }

    println(scores.entries.sortedByDescending { it.value }.first())

}