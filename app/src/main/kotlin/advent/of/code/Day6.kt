package advent.of.code

import advent.of.code.util.*

object Day6 {
    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.split(String::isBlank)
            .map { it.map(String::asIterable).flatten() }
            .map { it.distinct().count() }
            .sum()
}
