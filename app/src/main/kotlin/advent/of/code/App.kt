package advent.of.code

import advent.of.code.Day2

fun main(args: Array<String>) {
    val result = when (Pair(args[0], args[1])) {
        Pair("day2", "puzzle1") -> Day2.puzzle1(args.slice(2..(args.size - 1)))
        Pair("day2", "puzzle2") -> Day2.puzzle2(args.slice(2..(args.size - 1)))
        else -> null
    }
    println(result)
}
