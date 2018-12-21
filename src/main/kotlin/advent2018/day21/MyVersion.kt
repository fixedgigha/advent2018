package advent2018.day21

fun main(vararg args: String) {

    var three = 123
    while (true) {
        if (three and 456 == 72) {
            three = 0
            var four = 65536 // thre  and 65 //536
            three = 1107552
            var five = four and 255 // 0
            three += five // 1107552
            three = three and 16777215 //1107552
            three *= 65899 // -27874784
            five = if (256 > four) 1 else 0
            if (five == 1) {
                // GOTO  27
            }
            five = 0 // alreddy is
            while (true) { // --> 26 (llop back)
                var one = five + 1 // 1  --> 18
                one *= 256 // 256
                one = if (one > four) 1 else 0
                if (one == 0) {
                    five += 1 // -- 35
                }
            }
        }
        else {
            continue
        }
    }
}

