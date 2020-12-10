package advent.of.code

import advent.of.code.util.*

object Day10 {
    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        (sequenceOf(0) + inputLines.map(String::toInt))
            .sorted()
            .zipWithNext()
            .map { (a, b) -> b - a }
            .apply { all { it < 4 } || throw IllegalArgumentException() }
            .let { it.count { it == 1 } * (it.count { it == 3 } + 1)  }

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>): Int {
        return 0
    }
}
