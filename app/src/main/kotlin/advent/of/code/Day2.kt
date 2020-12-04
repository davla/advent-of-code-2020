package advent.of.code

import java.io.File

object Day2 {

private data class PasswordPolicy(val min: Int, val max: Int, val char: Char)

private val lineExtractor = "(\\d+)-(\\d+)\\s+(\\w):\\s+(\\w+)".toRegex()

private fun parseLine(line: String) =
    lineExtractor.matchEntire(line)!!.destructured
        .let { (min, max, char, password) ->
            Pair(PasswordPolicy(min.toInt(), max.toInt(), char.first()), password)
        }

fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
    inputLines.map(::parseLine)
        .filter { (policy, password) ->
            val occurrences = password.count { it == policy.char }
            policy.min <= occurrences && policy.max >= occurrences
        }.toList().size.toString()

fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
    inputLines.map(::parseLine)
        .filter { (policy, password) ->
            password.slice(listOf(policy.min - 1, policy.max - 1))
                .count { it == policy.char } == 1
        }.toList().size

}
