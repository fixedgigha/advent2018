package advent2018.day16

import java.io.File
import java.lang.RuntimeException

val operationsMap = mutableMapOf<Int, MutableSet<(List<Int>, List<Int>) -> List<Int>>>()

fun addr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] + input[instruction[2]]
    return response
}

fun addi(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] + instruction[2]
    return response
}

fun mulr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] * input[instruction[2]]
    return response
}

fun muli(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] * instruction[2]
    return response
}

fun banr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] and input[instruction[2]]
    return response
}

fun bani(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] and instruction[2]
    return response
}

fun borr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] or input[instruction[2]]
    return response
}

fun bori(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]] or instruction[2]
    return response
}

fun setr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = input[instruction[1]]
    return response
}

fun seti(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = instruction[1]
    return response
}

fun gtir(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = if (instruction[1] > input[instruction[2]]) 1 else 0
    return response
}

fun gtri(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = if (input[instruction[1]] > instruction[2]) 1 else 0
    return response
}

fun gtrr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = if (input[instruction[1]] > input[instruction[2]]) 1 else 0
    return response
}

fun eqir(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = if (instruction[1] == input[instruction[2]]) 1 else 0
    return response
}

fun eqri(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = if (input[instruction[1]] == instruction[2]) 1 else 0
    return response
}

fun eqrr(input: List<Int>, instruction: List<Int>): List<Int> {
    val response = mutableListOf<Int>()
    response.addAll(input)
    response[instruction[3]] = if (input[instruction[1]] == input[instruction[2]]) 1 else 0
    return response
}

val operations = listOf(
    ::addr, ::addi, ::mulr, ::muli, ::banr, ::bani, ::borr, ::bori,
    ::setr, ::seti, ::gtir, ::gtri, ::gtrr, ::eqir, ::eqri, ::eqrr
)


fun processSample(lines: List<String>): Int {
    val before = lines[0].filter { it.isDigit() }.map { it.toString().toInt()}
    val after = lines[2].filter { it.isDigit() }.map { it.toString().toInt()}
    val instruction = lines[1].split(" ").map { it.toString().toInt()}

    val candidates =  operations.filter{ it(before, instruction) == after }
    val default = mutableSetOf<(List<Int>, List<Int>) -> List<Int>>()
    operationsMap.getOrPut(instruction[0]) {default}.addAll(candidates)
    return candidates.size
}

fun noop(input: List<Int>, instruction: List<Int>): List<Int> {
    throw RuntimeException()
}

val realOperationsMap = mutableMapOf<Int, (List<Int>, List<Int>) -> List<Int>>(
    9 to ::gtrr, 3 to ::eqir, 11 to ::gtri, 1 to ::eqrr, 12 to ::eqri, 8 to ::gtir, 2 to ::setr, 0 to ::banr,
    6 to ::bani, 15 to ::seti, 14 to ::mulr, 5 to ::muli, 10 to ::addi, 13 to ::addr, 7 to ::borr, 4 to ::bori

)

fun main(vararg args: String) {
    var lines = File("src/main/resources/day16/example.txt").readLines()

    var count = 0

    while(lines.isNotEmpty()) {
        val sample = lines.take(4)
        processSample(sample)
        lines = lines.drop(4)
    }

    println("Result $realOperationsMap")

    var registers = listOf(0, 0, 0, 0)

    File("src/main/resources/day16/testprogram.txt").readLines().forEach { line ->
        val input = line.split(" ").map {it.toInt()}
        val op = realOperationsMap.getOrDefault(input[0], ::noop)
        registers= op(registers, input)
    }

    println("Final registers $registers")
}