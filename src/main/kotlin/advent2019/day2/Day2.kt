package advent2019.day2
import advent2018.inputMatch
import java.io.File

fun main() {
    val orig = File("src/main/resources/2019.day2/input.txt").readText().split(",").map {it.toInt()}
    (0..99).forEach { noun ->
        (0..99).forEach { verb ->
            val input = MutableList(orig.size) { orig[it] }
            input[1] = noun
            input[2] = verb
            var finished = false
            var x = 0
            while (!finished) {
                val opcode = input[x]
                val op1 = input[input[x + 1]]
                val op2 = input[input[x + 2]]
                val dest = input[x + 3]
                val update = when (opcode) {
                    99 -> {
                        finished = true; 0
                    }
                    1 -> op1 + op2
                    2 -> op1 * op2
                    else -> 0
                }
                if (finished) break
                input[dest] = update
                x += 4
            }
            if (input[0] == 19690720) {
                println("Result is $verb $noun")
                println("Result is ${100 * noun + verb}")
            }
        }}
}