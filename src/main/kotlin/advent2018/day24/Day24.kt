package advent2018.day24

import java.io.File

val groupRegex = Regex("(\\d+) units each with (\\d+) hit points (.*)with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)")

data class Group(var units: Int, var hitp: Int, val atkType: String, val damage: Int, val initv: Int, val immune: List<String>, val weak:  List<String>)

val immune =  mutableListOf<Group>()
val infect = mutableListOf<Group>()

val notesRegex = Regex("(immune|weak) to (.*)")

fun processNotes(input: String): Pair<List<String>, List<String>> {

    return if (input.trim().isEmpty()) {
        Pair(emptyList(), emptyList())
    }
    else {
        val weak = mutableListOf<String>()
        val immune = mutableListOf<String>()
        input.trim(' ').trim('(').trim(')').split(";").forEach { item ->
            notesRegex.matchEntire(item)?. let {match ->
                val loadGroup = if (match.groupValues[1] == "immune") immune else weak
                loadGroup.addAll(match.groupValues[2].split(", "))
            }
        }
        Pair(weak, immune)
    }
}

fun loadGroups(fileName: String) {
    var loadGroup =  mutableListOf<Group>()
    File(fileName).readLines().forEach {line ->
        when (line) {
            "Immune System:" -> loadGroup = immune
            "Infection:" -> loadGroup = infect
            else -> groupRegex.matchEntire(line) ?. let {match ->
                val units = match.groupValues[1].toInt()
                val hitp = match.groupValues[2].toInt()
                val notes = match.groupValues[3]
                val damage = match.groupValues[4].toInt()
                val atkType = match.groupValues[5]
                val initiative = match.groupValues[6].toInt()
                val (weak, immune) = processNotes(notes)
                loadGroup.add(Group(units, hitp, atkType, damage, initiative, immune, weak))
            }
        }
    }
}

fun main(vararg args: String) {
    loadGroups("src/main/resources/day24/input.txt")
    println("Immune:")
    immune.forEach {
        println(it)
    }
    println("Infect:")
    infect.forEach {
        println(it)
    }

}