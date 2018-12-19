package advent2018.day19

import java.io.File


fun main(vararg args: String) {
    val input = File("src/main/resources/day19/input.txt").readLines()
    var registers = //listOf(0, 10551343, 0, 3, 959213, 11)
    listOf(1, 0, 0, 0, 0, 0)
        //listOf(0, 10551343, 0, 5, 724973, 2)
    var ipr = Regex("#ip (\\d)").matchEntire(input[0])?.let { it.groupValues[1].toInt() }  ?: -1
    val program = input.subList(1, input.size)

    var count = 0
    while (count++ < 1000) {
        val instructionIndex = registers[ipr]
        if (instructionIndex == 3) {
            val z = 1
        }
        val instruction = program.getOrNull(instructionIndex)
        if (instruction != null) {
            val (op, codes) = Regex("(\\w{4}) (\\d+) (\\d+) (\\d+)").matchEntire(instruction)?.let {mr ->
                Pair(mr.groupValues[1],
                    (2..4).fold(mutableListOf(0)) {list, index ->
                        list.add(mr.groupValues[index].toInt())
                        list
                    })
            }?: Pair("crap", emptyList<Int>())
            if (op == "mulr" && codes == listOf(0, 5, 4, 2)) {
                val z = 1
            }
            val newRegisters = mutableListOf<Int>()
            val func = realOperationsMap[op]
            if (func != null) {
                newRegisters.addAll(func(registers, codes))
                println("ip=${registers[ipr]} $registers $op ${codes.subList(1, 4).joinToString(" ")} $newRegisters")
                newRegisters[ipr] = newRegisters[ipr] + 1
                registers = newRegisters
            }
            else {
                println("UNKNOWN OPERATION $op")
            }
        }
        else {
            break
        }
    }

    println("Final registers $registers")
}