package advent2018.day24

import java.io.File

val groupRegex = Regex("(\\d+) units each with (\\d+) hit points (.*)with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)")

val immuneBoost = 39

data class Group(val group: String,
                 val order: Int,
                 var units: Int,
                 val hitp: Int,
                 val atkType: String,
                 val damage: Int,
                 val initv: Int,
                 val immune: List<String>,
                 val weak:  List<String>): Comparable<Group>{
    override fun compareTo(other: Group): Int {
        val myEff = effectPower()
        val otherEff = other.effectPower()
        val result = when {
            myEff > otherEff -> -1
            myEff < otherEff -> 1
            else -> if (initv > other.initv) -1 else 1
        }
        return result
    }

    fun effectPower() = units * damage

    fun isAlive() = units > 0

    fun damageTo(target: Group): Int {
        val damage =
        when {
            target.immune.contains(atkType) -> 0
            target.weak.contains(atkType) -> 2 * effectPower()
            else -> effectPower()
        }
        return damage
    }

    fun receiveDamage(hits: Int) {
        units -= Math.min(hits / hitp, units)
    }

    fun attack(target: Group) {
        if (isAlive()) {
            val damageTo = damageTo(target)
            println("ATTACK:  $group$order -> ${target.group + target.order} : $damageTo ${Math.min(damageTo / target.hitp, target.units)}")
            target.receiveDamage(damageTo)
        }
    }

    fun chooseTarget(workingList: List<Group>): Group? {
        var mostDamage = 0
        val target = if (isAlive() && workingList.isNotEmpty()) {
            val g0 = workingList.sortedByDescending { damageTo(it) }
            mostDamage = damageTo(g0.first())
            if (mostDamage > 0) {
                val g1 = g0.takeWhile { damageTo(it) == mostDamage }
                val g2 = g1.sorted()
                g2.first()
            }
            else {
                null
            }

        }
        else {
            null
        }
        println("CHOICE: $group$order -> ${target?.group + target?.order} : $mostDamage")
        return target
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
        val trimmed = input.trim(' ').trim('(').trim(')')
        val splits = trimmed.split("; ")
        splits.forEach { item ->
            notesRegex.matchEntire(item)?. let {match ->
                val loadGroup = if (match.groupValues[1] == "immune") immune else weak
                val tokens = match.groupValues[2].split(", ")
                loadGroup.addAll(tokens)
            }
        }
        Pair(weak, immune)
    }
}

fun loadGroups(fileName: String) {
    var loadGroup =  mutableListOf<Group>()
    var teamLabel = ""
    File(fileName).readLines().forEach {line ->
        when (line) {
            "Immune System:" -> { loadGroup = immune; teamLabel = "Immune" }
            "Infection:" -> { loadGroup = infect; teamLabel = "Infect" }
            else -> groupRegex.matchEntire(line) ?. let {match ->
                val units = match.groupValues[1].toInt()
                val hitp = match.groupValues[2].toInt()
                val notes = match.groupValues[3]
                val damage = match.groupValues[4].toInt() + if (teamLabel == "Immune") immuneBoost else 0
                val atkType = match.groupValues[5]
                val initiative = match.groupValues[6].toInt()
                val (weak, immune) = processNotes(notes)
                loadGroup.add(Group(teamLabel, loadGroup.size + 1, units, hitp, atkType, damage, initiative, immune, weak))
            }
        }
    }
}

fun chooseTargets(from: List<Group>, to: List<Group>) : Map<Group, Group> {
    val result = mutableMapOf<Group, Group>()
    val workingList = mutableListOf<Group>()
    workingList.addAll(to.filter{ it.isAlive() }.sorted() )
    from.sorted().forEach {attacker ->
        if (workingList.isNotEmpty()) {
            val target = attacker.chooseTarget(workingList)
            if (target != null) {
                workingList.remove(target)
                result[attacker] = target
            }
        }
    }

    return result
}

fun round(): Boolean {
    val superGroup = mutableMapOf<Group, Group>()
    val imnToInf = chooseTargets(immune, infect)
    val infToImn = chooseTargets(infect, immune)
    superGroup.putAll(imnToInf)
    superGroup.putAll(infToImn)
    val order = superGroup.keys.toList().sortedByDescending { it.initv }
    val attacks = order.map { Pair(it, superGroup[it]) }
    attacks.forEach { (attacker, target) -> if (target != null) attacker.attack(target) }
    return immune.firstOrNull { it.isAlive() } != null && infect.firstOrNull { it.isAlive() } != null
}

fun dump() {
    println("Immune:")
    immune.forEach {
        println(it)
    }
    println("Infect:")
    infect.forEach {
        println(it)
    }
}

fun main(vararg args: String) {
    loadGroups("src/main/resources/day24/input.txt")
    dump()
    while(round()) {
        dump()
    }
    dump()
    println("Immune score ${immune.map { it.units }.sum()}")
    println("Infect score ${infect.map { it.units }.sum()}")
    // 19293 too low
}