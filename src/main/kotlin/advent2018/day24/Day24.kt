package advent2018.day24

val groupRegex = Regex("(\\d+) units each with (\\d+) hit points \\((.*)\\) with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)")

data class Group(var units: Int, var hitp: Int, val atkType: String, val damage: Int, val initv: Int, val immune: List<String>, val weak:  List<String>)

val immune = List<Group>
val infect = List<Group>

fun main(vararg args: String) {
}