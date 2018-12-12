package advent2018.day12

fun add(x: Int, y: Int) = x + y

val initialState = "##..#.#.#..##..#..##..##..#.#....#.....##.#########...#.#..#..#....#.###.###....#..........###.#.#.."

val rules = """
..##. => .
..... => .
##..# => .
...#. => .
#.... => .
...## => #
.#.#. => .
#..#. => #
##.#. => .
#..## => .
..#.. => .
#.#.# => .
###.# => .
###.. => .
.#... => #
.##.# => .
##... => #
..### => .
####. => .
#...# => #
.#..# => #
##### => #
..#.# => #
.#.## => #
#.### => .
....# => .
.###. => .
.#### => #
.##.. => .
##.## => #
#.##. => #
#.#.. => #
""".trimIndent()

val initialStateTest = "#..#.#..##......###...###"

val rulesTest = """
...## => #
..#.. => #
.#... => #
.#.#. => #
.#.## => #
.##.. => #
.#### => #
#.#.# => #
#.### => #
##.#. => #
##.## => #
###.. => #
###.# => #
####. => #
""".trimIndent()

data class Rule(val match: List<Int>, val result: Int)

val deadRule = Rule(emptyList(), -1)

fun hashToInt(c: Char) = if (c == '#') 1 else 0
fun hashToInt(mr: MatchResult, x: Int) = hashToInt(mr.groupValues[x][0])

fun makeRules(rules: String): List<Rule> {
    val ruleRegex = Regex("([#\\.])([#\\.])([#\\.])([#\\.])([#\\.]) => ([#\\.])")
    return rules.lines().map {line ->
        ruleRegex.matchEntire(line) ?.let {mr ->
            Rule(
                (1 ..5).map { x -> hashToInt(mr, x) },
                hashToInt(mr, 6)
            )
        }?: deadRule
    }
}

fun match(index: Int, state: List<Int>, rules: List<Rule>): Int {
    for (rule in rules) {
        val test = state.slice((index - 2)..(index + 2))
        if (test == rule.match) return rule.result
    }
    return 0
}

fun makeInitialState(input: String, pad: Int): List<Int> {
    val result = mutableListOf<Int>()
    result.addAll((1..pad).map{0})
    result.addAll(
        input.toCharArray().map { c -> hashToInt(c) }
    )
    result.addAll((1..pad).map{0})
    return result
}

fun stateToStr(input: List<Int>) = String(input.map{ if (it == 1) '#' else '.'}.toCharArray())

fun scoreResult(input: List<Int>, offset: Int) =
        input.mapIndexed { index, i -> if (i == 0) 0 else index - offset }.sum()

fun stateGrow(state: List<Int>, pad: Int): List<Int> {
    val padNeeded = state.size  - state.lastIndexOf(1)
    val newState =
    if (padNeeded < pad) {
        val s = mutableListOf<Int>()
        s.addAll(state)
        (1 .. padNeeded).forEach{_ -> s.add(0)}
        s
    }
    else {
        state
    }

    return newState
}

fun main(vararg args: String) {
    println("Hello")
    val r = makeRules(rules)
    println("Rules $r")
    var offSet = 6
    var state = makeInitialState(initialState, offSet)
    println("Initial state ${stateToStr(state)}")

    val scores = mutableListOf<Int>()
    (1..1000).forEach { gen ->
        state = stateGrow(
            state.mapIndexed { index, i ->
                if (index < 2 || index > state.size - 3)
                    0
                else
                    match(index, state, r)
            },
            offSet
        )
        val score = scoreResult(state, 6)
        scores.add(score)
    }
    println("Final state ${stateToStr(state)}")

    println(analyseScoreTrend(scores))
}

fun analyseScoreTrend(scores: MutableList<Int>): Pair<Boolean, Long> {
    val diffs = (1..100).map {scores[scores.size - it] - scores[scores.size - (it + 1)]}
    val constant = diffs.groupBy {it}.size == 1
    val trend = diffs[0]
    val result = ((50000000000L - scores.size) * trend) + scores.last()
    return Pair(constant, result)
}
