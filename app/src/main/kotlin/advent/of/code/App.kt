package advent.of.code

import java.io.File
import java.io.IOException

private val cwd = File(System.getProperty("user.dir")).toPath()

private fun findFile(filePath: String): File {
    val file = File(filePath)
    val path = file.toPath()

    if (path.isAbsolute()) {
        return file
    }

    val relativeFile = cwd.resolve(path).toFile()
    if (relativeFile.exists()) {
        return relativeFile
    }

    val inputsFile = cwd.resolve("../inputs").resolve(path).toFile()
    if (inputsFile.exists()) {
        return inputsFile
    }

    throw IOException("File $path does not exist")
}

private fun parseLines(path: String) =
    findFile(path).readLines().asSequence()

fun main(args: Array<String>) {
    val lines = parseLines(args.getOrNull(2) ?: "${args[0]}.txt")
    val puzzleArgs = args.slice(3..(args.size - 1))

    val result = when (Pair(args[0], args[1])) {
        Pair("day1", "puzzle1") -> Day1.puzzle1(lines, puzzleArgs)
        Pair("day1", "puzzle2") -> Day1.puzzle2(lines, puzzleArgs)
        Pair("day2", "puzzle1") -> Day2.puzzle1(lines, puzzleArgs)
        Pair("day2", "puzzle2") -> Day2.puzzle2(lines, puzzleArgs)
        Pair("day3", "puzzle1") -> Day3.puzzle1(lines, puzzleArgs)
        Pair("day3", "puzzle2") -> Day3.puzzle2(lines, puzzleArgs)
        Pair("day4", "puzzle1") -> Day4.puzzle1(lines, puzzleArgs)
        Pair("day4", "puzzle2") -> Day4.puzzle2(lines, puzzleArgs)
        Pair("day5", "puzzle1") -> Day5.puzzle1(lines, puzzleArgs)
        Pair("day5", "puzzle2") -> Day5.puzzle2(lines, puzzleArgs)
        Pair("day6", "puzzle1") -> Day6.puzzle1(lines, puzzleArgs)
        Pair("day6", "puzzle2") -> Day6.puzzle2(lines, puzzleArgs)
        Pair("day7", "puzzle1") -> Day7.puzzle1(lines, puzzleArgs)
        Pair("day7", "puzzle2") -> Day7.puzzle2(lines, puzzleArgs)
        Pair("day8", "puzzle1") -> Day8.puzzle1(lines, puzzleArgs)
        Pair("day8", "puzzle2") -> Day8.puzzle2(lines, puzzleArgs)
        else -> null
    }
    println(result)
}
