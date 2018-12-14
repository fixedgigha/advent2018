package advent2018.day14


data class Node(val value: Int, var next: Node? = null )

val NODE_OF_DOOM = Node(Int.MIN_VALUE)

val start = Node(3, Node(7))
var end = start.next?: NODE_OF_DOOM
var nodeCount = 2

data class Elf(var node: Node)

var elf1 = Elf(start)
var elf2 = Elf(end)

fun move(elf: Elf) {
    var nextNode = elf.node
    (0..elf.node.value).forEach {_ ->
        nextNode = nextNode.next ?: start
    }
    elf.node =  nextNode
}

const val target  = 236021

fun main(vararg args: String) {
    while (nodeCount < target + 10) {
        val newScore = elf1.node.value + elf2.node.value
        if (newScore < 10) {
            append(newScore)
        }
        else {
            append(newScore / 10)
            append(newScore % 10)
        }
        move(elf1)
        move(elf2)
    }
    var startResult: Node = start
    (1..target).forEach { _ ->
        startResult = startResult.next?: NODE_OF_DOOM
    }
    (1..10).forEach { _ ->
        print(startResult.value)
        startResult = startResult.next?: NODE_OF_DOOM
    }
    println()
}



