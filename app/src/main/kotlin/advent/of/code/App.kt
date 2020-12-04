package advent.of.code

import java.io.File

private fun parseLines(path: String) =
    File(path).readLines().asSequence()

fun main(args: Array<String>) {
    val lines = parseLines(args[2])
    val puzzleArgs = args.slice(3..(args.size - 1))

    val result = when (Pair(args[0], args[1])) {
        Pair("day2", "puzzle1") -> Day2.puzzle1(lines, puzzleArgs)
        Pair("day2", "puzzle2") -> Day2.puzzle2(lines, puzzleArgs)
        Pair("day3", "puzzle1") -> Day3.puzzle1(lines, puzzleArgs)
        Pair("day3", "puzzle2") -> Day3.puzzle2(lines, puzzleArgs)
        Pair("day4", "puzzle1") -> Day4.puzzle1(lines, puzzleArgs)
        Pair("day4", "puzzle2") -> Day4.puzzle2(lines, puzzleArgs)
        else -> null
    }
    println(result)
}
