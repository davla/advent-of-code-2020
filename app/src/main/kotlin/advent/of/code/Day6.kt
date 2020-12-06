package advent.of.code

import advent.of.code.util.*

object Day6 {
    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.split(String::isBlank)
            .map { it.map(String::asIterable).flatten() }
            .sumBy { it.distinct().count() }

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.split(String::isBlank)
            .map { it.map(String::asIterable) }
            .map { it.reduce(Iterable<Char>::intersect) }
            .sumBy { it.count() }
}
