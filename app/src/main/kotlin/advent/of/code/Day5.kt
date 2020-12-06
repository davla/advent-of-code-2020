package advent.of.code

import advent.of.code.util.*

object Day5 {

    private val rowRange = 0..127
    private val colRange = 0..7
    private val rowDirections = Pair('F', 'B')
    private val colDirections = Pair('L', 'R')

    private fun parseLine(line: String) =
        line.asIterable().partitionWhile { it in rowDirections.asIterable() }

    private fun search(directions: Collection<Char>,
                       values: Pair<Char, Char>,
                       range: IntProgression): Int =
        if (range.count() == 1) {
            range.first
        }
        else {
            search(directions.drop(1), values,
                when (directions.first()) {
                    values.first -> range.lowerHalf()
                    values.second -> range.upperHalf()
                    else -> throw IllegalArgumentException()
                })
        }

    private fun parseInput(inputLines: Sequence<String>) =
        inputLines.map(::parseLine)
            .map { Pair<Int, Int>(
                    search(it.first.toList(), rowDirections, rowRange),
                    search(it.second.toList(), colDirections, colRange))
            }.map { (row, col) -> row * 8 + col }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        parseInput(inputLines).maxOrNull()

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        (parseInput(inputLines)
            .sorted()
            .zipWithNext()
            .find { (a, b) -> a + 1 < b }
            ?: throw IllegalArgumentException())
            .let { (a, _) -> a + 1 }


}
