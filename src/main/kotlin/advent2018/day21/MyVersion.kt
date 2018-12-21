package advent2018.day21

fun main(vararg args: String) {
    val killSet = mutableSetOf<Int>()
    var firstKiller = Int.MIN_VALUE
    var lastKiller = Int.MAX_VALUE
    var zero = 0
    //var one = 0
    //var two = zero
    var three = 123 // --> 0
    var four = 0
    var five = 0
    while (true) { // --> loop back from 3 4
        three = three and 456 // 123 | 456 = 72--> 1
        if (three == 72) // --> 2
            break
    }
    three = 0 // --> 5
    var programContinuing = true
    while (programContinuing) {
        four = three or 65536 // three  and 65536 = 65536 --> 6
        three = 1107552 // --> 7
        while (true) {
            // LOOP to HERE FROm  BOTTOM?
            //five = four and 255 // 0 --> 8
            three = (((three + (four and 255)) // 1107552 --> 9
                    and 16777215) //1107552 -> 10
                    * 65899 // -27874784 -> 11
                    ) and 16777215 // 5679648  -> 12
            //five = if (256 > four) 1 else 0 // -> 13
            if (256 > four) {  // --> 14 15
                if (killSet.isEmpty()) {
                    firstKiller = three
                }
                if (!killSet.add(three)) {
                    lastKiller = three
                    programContinuing = false
                }
                //programContinuing = three != zero
                break
            }
            five = 0 // alreddy is --> 17
            while (true) { // --> 25 (loop back)
                if ((five + 1) * 256 > four) { // -> 20
                    four = five // --> 26
                    break // goto 6
                }
                five += 1 // --> 24
            }
        }
    }
    println("Part one result $firstKiller")
    println("Part two result $lastKiller")

}

