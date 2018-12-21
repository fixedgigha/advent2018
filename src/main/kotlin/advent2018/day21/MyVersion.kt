package advent2018.day21

fun main(vararg args: String) {

    var three = 123
    while (true) {
        if (three and 456 == 72) {
            three = 0
            var four = 65536
            three = 1107552
            var five = four and 255 // 0
            three += five

        }
        else {
            continue
        }
    }
}

