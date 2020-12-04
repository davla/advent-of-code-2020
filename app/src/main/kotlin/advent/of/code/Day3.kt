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

    private fun parseLine(line: String) =
        line.map(Tile::fromChar)

    private fun parseInput(path: String) =
        File(path).readLines().asSequence()
            .map(::parseLine)
            .toList()

    private fun navigate(map: Collection<Collection<Tile>>): Int =
        map.fold(Pair(0, 0)) { (trees, x), line ->
            Pair(
                if (line.elementAt(x) == Tile.TREE) trees + 1 else trees,
                (x + 3) % line.size
            )
        }.first

    fun puzzle1(args: Iterable<String>) =
        navigate(parseInput(args.first())
}
