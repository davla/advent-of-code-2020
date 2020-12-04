package advent.of.code

fun main(args: Array<String>) {
    val result = when (Pair(args[0], args[1])) {
        Pair("day2", "puzzle1") -> Day2.puzzle1(args.slice(2..(args.size - 1)))
        Pair("day2", "puzzle2") -> Day2.puzzle2(args.slice(2..(args.size - 1)))
        Pair("day3", "puzzle1") -> Day3.puzzle1(args.slice(2..(args.size - 1)))
        Pair("day3", "puzzle2") -> Day3.puzzle2(args.slice(2..(args.size - 1)))
        Pair("day4", "puzzle1") -> Day4.puzzle1(args.slice(2..(args.size - 1)))
        else -> null
    }
    println(result)
}
