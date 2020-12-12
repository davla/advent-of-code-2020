package advent.of.code

import advent.of.code.util.*

object Day11 {
    private enum class Tile {
        FREE,
        OCCUPIED,
        FLOOR;

        companion object {
            fun parse(c: Char) =
                when (c) {
                    'L' -> FREE
                    '#' -> OCCUPIED
                    '.' -> FLOOR
                    else -> throw IllegalArgumentException()
                }
        }
    }

    private fun applyRules(inputs: Matrix<Tile>) =
        inputs.mapIndexed { (a, b), item ->
            val adjacent = listOf(
                inputs.elementAtOrNull(a - 1, b - 1),
                inputs.elementAtOrNull(a - 1, b),
                inputs.elementAtOrNull(a - 1, b + 1),
                inputs.elementAtOrNull(a, b - 1),
                inputs.elementAtOrNull(a, b + 1),
                inputs.elementAtOrNull(a + 1, b - 1),
                inputs.elementAtOrNull(a + 1, b),
                inputs.elementAtOrNull(a + 1, b + 1)
            ).filterNotNull()
            when (item) {
                Tile.FREE -> if (adjacent.none { it == Tile.OCCUPIED })
                    Tile.OCCUPIED else item
                Tile.OCCUPIED -> if (adjacent.count { it == Tile.OCCUPIED } >= 4)
                    Tile.FREE else item
                else -> item
            }
        }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>): Int {
        val inputs = Matrix(inputLines.map { it.map(Tile::parse) }.toList())
        val rounds = generateSequence(inputs, { applyRules(it) })
        return (rounds.zipWithNext()
            .find { (a, b) -> a == b }
            ?: throw IllegalArgumentException())
            .first
            .count { it == Tile.OCCUPIED }
    }










    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) = 5

}
