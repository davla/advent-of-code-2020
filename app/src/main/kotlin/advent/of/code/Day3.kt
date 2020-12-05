package advent.of.code

import java.io.File

object Day3 {
    private enum class Tile {
        OPEN,
        TREE;

        companion object {
            fun fromChar(char: Char) =
                when (char) {
                    '.' -> OPEN
                    '#' -> TREE
                    else -> throw IllegalArgumentException()
                }
        }
    }

    private val coordsProgressions = listOf<Pair<(Int) -> Int, (Int) -> IntProgression>>(
        Pair({ it + 1 }, { 0 until it }),
        Pair({ it + 3 }, { 0 until it }),
        Pair({ it + 5 }, { 0 until it }),
        Pair({ it + 7 }, { 0 until it }),
        Pair({ it + 1 }, { 0 until it step 2 })
    )

    private fun parseLine(line: String) =
        line.map(Tile::fromChar)

    private fun navigate(map: Sequence<Collection<Tile>>,
                         nextX: (Int) -> Int): Int =
        map.fold(Pair(0, 0)) { (trees, x), line ->
            Pair(
                if (line.elementAt(x) == Tile.TREE) trees + 1 else trees,
                nextX(x) % line.size
            )
        }.first

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        navigate(inputLines.map(::parseLine), { it + 3 })

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.map(::parseLine)
            .let { lines ->
                coordsProgressions.map { (nextX, yIndices) ->
                    val linesSeq = lines.toList().slice(yIndices(lines.count()))
                        .asSequence()
                    navigate(linesSeq, nextX)
                }
            }.map(Int::toLong)
            .reduce(Long::times)

}
