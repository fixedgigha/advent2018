package advent2018.day4

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.MINUTES

val inputRegex = Regex("\\[(\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d)] (.*)")
val guardRegex = Regex("Guard #(\\d+) begins shift")
val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

data class Sleep(val sleep: LocalDateTime, var wake: LocalDateTime? = null)

class GuardSummary(val guard: Int, events: List<Sleep>) {
    val totalSleep: Long
    val mostFrequentMinute: Map.Entry<Int, Int>

    init {
        var sleep = 0L
        val minuteCount = events.fold(mutableMapOf<Int, Int>()) {map, event ->
            sleep += MINUTES.between(event.sleep, event.wake)
            var time = event.sleep
            while (time < event.wake) {
                val minute = time.minute
                map[minute] = map.getOrDefault(minute, 0) + 1
                time = time.plusMinutes(1)
            }
            map
        }
        totalSleep = sleep
        mostFrequentMinute = minuteCount.entries.sortedByDescending { it.value }.first()
    }
}

fun linesToSleepMap(lines : List<String>): Map<Int, List<Sleep>> =
    lines
        .sorted()
        .fold(Pair(mutableMapOf<Int, MutableList<Sleep>>(), 0)) { (events, currentGuardInt), line ->
            val lineMatch = inputRegex.matchEntire(line)
            var nextGuardInt = currentGuardInt
            if (lineMatch != null) {
                val guardMatch = guardRegex.matchEntire(lineMatch.groupValues[2])
                if (guardMatch != null) {
                    nextGuardInt = guardMatch.groupValues[1].toInt()
                }
                else {
                    val time = LocalDateTime.parse(lineMatch.groupValues[1], timeFormat)
                    val guardEvents = events.getOrDefault(currentGuardInt, mutableListOf())
                    if (lineMatch.groupValues[2] == "falls asleep") {
                        guardEvents.add(Sleep(time))

                    }
                    else {
                        guardEvents.last().wake = time
                    }
                    events[currentGuardInt] = guardEvents
                }
            }
            Pair(events, nextGuardInt)
        }.first

fun main(args: Array<String>) {
    val lines = File("src/main/resources/day4/input.txt").readLines()
    val finalMap = linesToSleepMap(lines)
    val summaries = finalMap.map { it.key to GuardSummary(it.key, it.value) }.toMap()
    val winner = summaries.values.sortedByDescending { it.totalSleep } .first()
    val bestMinute = winner.mostFrequentMinute.key

    println(winner.guard * bestMinute)

    val highestFreq = summaries.values.sortedByDescending { it.mostFrequentMinute.value }
    val freqWinner = highestFreq.first()

    println(freqWinner.guard * (freqWinner.mostFrequentMinute.key))
}