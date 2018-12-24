package advent2018.day24

import java.io.File

val groupRegex = Regex("(\\d+) units each with (\\d+) hit points (.*)with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)")

data class Group(var units: Int,
                 var hitp: Int,
                 val atkType: String,
                 val damage: Int,
                 val initv: Int,
                 val immune: List<String>,
                 val weak:  List<String>): Comparable<Group>{
    override fun compareTo(other: Group) =
        when {
            effectPower() > other.effectPower() -> -1
            effectPower() < other.effectPower() -> 1
            else -> if (initv > other.initv) -1 else 1
        }

    fun effectPower() = units * damage

    fun isAlive() = units > 0

    fun damageTo(target: Group) =
        when {
            target.immune.contains(atkType) -> 0
            target.weak.contains(atkType) -> 2 * effectPower()
            else -> effectPower()
        }
}

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

fun chooseTargets(from: List<Group>, to: List<Group>) : Map<Group, Group> {
    val result = mutableMapOf<Group, Group>()
    val workingList = mutableListOf<Group>()
    workingList.addAll(to.filter{ it.isAlive() }.sorted() )
    from.forEach {attacker ->
        if (workingList.isNotEmpty()) {
            workingList.sortByDescending { attacker.damageTo(it) }
            val mostDamage = attacker.damageTo(workingList.first())
            val target = workingList.takeWhile { attacker.damageTo(it) == mostDamage }.sorted().first()
            workingList.remove(target)
            result[attacker] = target
        }
    }

    return result
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