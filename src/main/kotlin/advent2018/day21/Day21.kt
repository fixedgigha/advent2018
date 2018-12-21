package advent2018.day21

import advent2018.day19.realOperationsMap
import java.time.LocalDateTime

val program = """
#ip 2
seti 123 0 3
bani 3 456 3
eqri 3 72 3
addr 3 2 2
seti 0 0 2
seti 0 4 3
bori 3 65536 4
seti 1107552 3 3
bani 4 255 5
addr 3 5 3
bani 3 16777215 3
muli 3 65899 3
bani 3 16777215 3
gtir 256 4 5
addr 5 2 2
addi 2 1 2
seti 27 0 2
seti 0 2 5
addi 5 1 1
muli 1 256 1
gtrr 1 4 1
addr 1 2 2
addi 2 1 2
seti 25 3 2
addi 5 1 5
seti 17 3 2
setr 5 3 4
seti 7 4 2
eqrr 3 0 5
addr 5 2 2
seti 5 8 2""".trimIndent()

val killers = mutableSetOf<Int>()
var lastKiller = -1
fun main(vararg args: String) {
    val input = program.lines()
    var registers = listOf(0, 0, 0, 0, 0, 0)
    var ipr = Regex("#ip (\\d)").matchEntire(input[0])?.let { it.groupValues[1].toInt() }  ?: -1
    val program = input.subList(1, input.size)

    val stopAt = LocalDateTime.now().plusMinutes(30)
    while (LocalDateTime.now() < stopAt) {
        val instruction = program.getOrNull(registers[ipr])
        if (instruction == "gtir 256 4 5") {
            if (registers[4] < 256) {
                if (lastKiller == -1) {
                    println("First killer ${registers[3]}")
                }
                if (killers.add(registers[3])) {
                    lastKiller = registers[3]
                    println("Killer ${lastKiller} at ${LocalDateTime.now()}")
                }
                else
                    println("Duplicate ${registers[3]} at ${killers.size} last killer $lastKiller")
                //println("Completion test registers = ${registers.subList(3, 5)}")
            }
        }
        if (instruction != null) {
            val (op, codes) = Regex("(\\w{4}) (\\d+) (\\d+) (\\d+)").matchEntire(instruction)?.let {mr ->
                Pair(mr.groupValues[1],
                    (2..4).fold(mutableListOf(0)) {list, index ->
                        list.add(mr.groupValues[index].toInt())
                        list
                    })
            }?: Pair("crap", emptyList<Int>())
            val newRegisters = mutableListOf<Int>()
            val func = realOperationsMap[op]
            if (func != null) {
                newRegisters.addAll(func(registers, codes))
             //   println("ip=${registers[ipr]} $registers $op ${codes.subList(1, 4).joinToString(" ")} $newRegisters")
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
    println("There are ${killers.size} killers ${killers.min()} ${killers.last()}")

}