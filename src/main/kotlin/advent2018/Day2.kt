package advent2018

import java.io.File


fun charCounts(charArr : CharArray) =
    charArr.groupBy { it }
        .values
        .map (List<Char>::size)
        .filter {l -> l == 2 || l == 3 }
        .distinct()

fun main(args : Array<String>) : Unit {
    val count = File("src/main/resources/day2/input.txt").readLines()
        .flatMap {l -> charCounts(l.toCharArray())}
        .groupBy { x -> x }
        //.collect(Collectors.groupingBy { it })


    println("Hello ${count.getOrDefault(2, emptyList()).size *
                    count.getOrDefault(3, emptyList()).size}")
}
