package advent.of.code

import advent.of.code.util.*

object Day9 {
    private val preambleLength = 25

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>): Long {
        val input = inputLines.map(String::toLong)
        return (input.windowed(preambleLength)
            .withIndex()
            .map { (index, window) ->
                Pair(input.elementAt(index + window.count()), window) }
            .find { (value, window) ->
                value !in window.cartesianProduct(window).map { it.sum() } }
            ?: throw IllegalArgumentException())
            .let { it.first }
    }
}
