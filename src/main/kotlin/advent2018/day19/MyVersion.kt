package advent2018.day19

fun main(vararg args: String)  {
    var zero = 1
    var one = 2 // 17   //10551343
    var two = 0
    one *= one // 18
    one *= 19 // 19
    one *= 11 // 20
    two += 4 // 21
    two *= 22 // 22
    two += 19 // 23
    one += two // 24
    two = 27 // 27
    two *= 28 // 28
    two += 29 // 29
    two *= 30 // 28
    two *= 14 // 31
    two *= 32 // 32
    one += two // 33
    println("After initialisation one  = $one")
    zero = 0
    var five = 1 // 1
    var four = 1 // 2
    var loopCount = 0
    while (true) {
        loopCount ++
        if (loopCount % 100000 == 0) {
            println("Loop $loopCount  zero is $zero four is $four five is $five")
        }
        if (four * five == one) { // 3 4 5
            println("Incrementing zero $zero -> ${zero + five}")
            zero += five
        }
        four += 1
        if (four <= one) { // 9
            continue
        }
        five += 1 // 14
        if (five <= one) {  // 15 16
            // goto instruction 1
            four = 1 // 2
            continue
        }
        else {
            break
        }
    }
    println("Final zero is $zero")
}