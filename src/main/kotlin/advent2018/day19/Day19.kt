package advent2018.day19

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

val realOperationsMap = mapOf<String, (List<Int>, List<Int>) -> List<Int>> (
    "gtrr" to ::gtrr, "eqir" to ::eqir, "gtri" to ::gtri, "eqrr" to ::eqrr, "eqri" to ::eqri, "gtir" to ::gtir,
    "setr" to ::setr, "banr" to ::banr,
    "bani" to ::bani, "seti" to ::seti, "mulr" to ::mulr, "muli" to ::muli, "addi" to ::addi, "addr" to ::addr,
    "borr" to ::borr, "bori" to ::bori
)



fun main(vararg args: String) {
    val input = File("src/main/resources/day19/testInput.txt").readLines()
    var registers = listOf(0, 0, 0, 0, 0, 0)
    var ip = Regex("#ip (\\d)").matchEntire(input[0])?.let { it.groupValues[1].toInt() }  ?: -1
    val program = input.subList(1, input.size)

    var count = 0
    while (true) {
        val instruction = program.getOrNull(registers[ip])
        if (instruction != null) {
            val (op, codes) = Regex("(\\w{4}) (\\d+(?: )?){3}").matchEntire(instruction)?.let {mr ->
                Pair(mr.groupValues[1],
                    (2..4).fold(mutableListOf(0)) {list, index ->
                        list.add(mr.groupValues[index].toInt())
                        list
                    })
            }?: Pair("crap", emptyList<Int>())
            val newRegisters = mutableListOf<Int>()
            newRegisters.addAll(realOperationsMap[op]?.let { it(registers,codes) } ?: registers)
            newRegisters[ip] = newRegisters[ip + 1]
            registers = newRegisters
        }
        else {
            break
        }
    }

    println("Final registers $registers")
}