package advent.of.code

import advent.of.code.util.*

object Day11 {
    private enum class Tile {
        FREE,
        OCCUPIED,
        FLOOR;

        fun isNotFloor() = this != FLOOR

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

    private fun applyRules(seats: Matrix<Tile>,
                           rules: (Matrix<Tile>, Matrix.Indices, Tile) -> Tile) =
        seats.mapIndexed { indices, tile -> rules(seats, indices, tile) }

    private fun occupiedAtEquilibrium(inputLines: Sequence<String>,
                                      rules: (Matrix<Tile>, Matrix.Indices, Tile) -> Tile) =
        (Matrix(inputLines.map { it.map(Tile::parse) }.toList())
            .let { generateSequence(it) { applyRules(it, rules) } }
            .zipWithNext()
            .find { (a, b) -> a == b }
            ?: throw IllegalArgumentException())
            .first
            .count { it == Tile.OCCUPIED }

    private fun puzzle1Adjacent(seats: Matrix<Tile>, indices: Matrix.Indices) =
        indices.let { (a, b) ->
            listOf(
                seats.elementAtOrNull(a - 1, b - 1),
                seats.elementAtOrNull(a - 1, b),
                seats.elementAtOrNull(a - 1, b + 1),
                seats.elementAtOrNull(a, b - 1),
                seats.elementAtOrNull(a, b + 1),
                seats.elementAtOrNull(a + 1, b - 1),
                seats.elementAtOrNull(a + 1, b),
                seats.elementAtOrNull(a + 1, b + 1)
            ).filterNotNull()
        }

    private fun puzzle2Adjacent(seats: Matrix<Tile>, indices: Matrix.Indices) =
        indices.let { (a, b) ->
            val (rows, cols) = seats.size
            val aRow = seats.rowAt(a).toList()
            val bCol = seats.colAt(b).toList()
            listOf(
                aRow.slice((b - 1) downTo 0).find(Tile::isNotFloor),
                aRow.slice((b + 1) until cols).find(Tile::isNotFloor),
                bCol.slice((a - 1) downTo 0).find(Tile::isNotFloor),
                bCol.slice((a + 1) until rows).find(Tile::isNotFloor),
                seats.slice((a - 1) downTo 0, (b - 1) downTo 0).find(Tile::isNotFloor),
                seats.slice((a - 1) downTo 0, (b + 1) until cols).find(Tile::isNotFloor),
                seats.slice((a + 1) until rows, (b - 1) downTo 0).find(Tile::isNotFloor),
                seats.slice((a + 1) until rows, (b + 1) until cols).find(Tile::isNotFloor)
            ).filterNotNull()
        }

    private fun rules(adjacent: (Matrix<Tile>, Matrix.Indices) -> Iterable<Tile>,
                      occupiedCount: Int): (Matrix<Tile>, Matrix.Indices, Tile) -> Tile =
        { seats, indices, tile ->
            val adjacentSeats = adjacent(seats, indices)
            when (tile) {
                Tile.FREE -> if (adjacentSeats.none { it == Tile.OCCUPIED })
                    Tile.OCCUPIED else tile
                Tile.OCCUPIED -> if (adjacentSeats.count { it == Tile.OCCUPIED }
                    >= occupiedCount) Tile.FREE else tile
                else -> tile
            }
        }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        occupiedAtEquilibrium(inputLines, rules(::puzzle1Adjacent, 4))

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        occupiedAtEquilibrium(inputLines, rules(::puzzle2Adjacent, 5))
}
