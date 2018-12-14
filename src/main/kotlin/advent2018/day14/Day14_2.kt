package advent2018.day14

var searchFrom: Node? = null

fun found() =
    searchFrom?.value == 2 &&
    searchFrom?.next?.value == 3 &&
    searchFrom?.next?.next?.value == 6 &&
    searchFrom?.next?.next?.next?.value == 0 &&
    searchFrom?.next?.next?.next?.next?.value == 2 &&
    searchFrom?.next?.next?.next?.next?.next?.value == 1

fun append(newScore: Int): Boolean {
    end.next = Node(newScore)
    end = end.next?: NODE_OF_DOOM
    searchFrom = searchFrom?.next
    nodeCount += 1
    return newScore == 1 && found()
}

fun main(vararg args: String) {
    var found = false
    while (!found) {
        val newScore = elf1.node.value + elf2.node.value
        if (newScore < 10) {
            found = append(newScore)
        }
        else {
            val newScore1 = newScore / 10
            val newScore2 = newScore % 10
            found = append(newScore1)
            found = found || append(newScore2)
        }
        if (nodeCount == 6) {
            searchFrom = start
        }
        move(elf1)
        move(elf2)
    }

    println(nodeCount)
}



