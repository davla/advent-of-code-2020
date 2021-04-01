package advent.of.code

import advent.of.code.util.gameoflife.gameOfLife
import advent.of.code.util.gameoflife.SurroundingEvolution
import advent.of.code.util.gameoflife.Indices

object Day17 {
    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>): Int =
        gameOfLife(
            seed = parseInput(inputLines, 3),
            evolve = SurroundingEvolution(
                getSurrounding = Indices::adjacent,
                evolveElement = ::evolve,
                default = Cube.INACTIVE
            ).asFunction()
        ).take(7)
        .last()
        .values
        .countActive()

    private fun evolve(cube: Cube, neighbors: Iterable<Cube>) =
        when (Pair(cube, neighbors.countActive())) {
            Pair(Cube.ACTIVE, 2), Pair(Cube.ACTIVE, 3),
                Pair(Cube.INACTIVE, 3) -> Cube.ACTIVE
            else -> Cube.INACTIVE
        }

    private fun parseInput(inputLines: Sequence<String>, dimensionCount: Int):
        Map<Indices, Cube> {

        val extraDimensions = LongArray(dimensionCount - 2) { 0L }
        return inputLines.flatMapIndexed { x, line ->
            line.asIterable().mapIndexed { y, char ->
                val indices = Indices(x.toLong(), y.toLong(), *extraDimensions)
                val cube = when(char) {
                    '.' -> Cube.INACTIVE
                    '#' -> Cube.ACTIVE
                    else -> throw IllegalArgumentException()
                }
                Pair(indices, cube)
            }
        }.toMap()
    }

    private fun Iterable<Cube>.countActive() = count { it == Cube.ACTIVE }

    private enum class Cube {
        ACTIVE,
        INACTIVE;
    }
}
