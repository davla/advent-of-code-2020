import java.io.File

data class PasswordPolicy(val min: Int, val max: Int, val char: Char)

val lineExtractor = "(\\d+)-(\\d+)\\s+(\\w):\\s+(\\w+)".toRegex()

fun parseLine(line: String) =
    lineExtractor.matchEntire(line)!!.destructured
        .let { (min, max, char, password) ->
            Pair(PasswordPolicy(min.toInt(), max.toInt(), char.first()), password)
        }

fun parseInput(path: String) =
    File(path).readLines().asSequence()
        .map(::parseLine)

val result = parseInput(args[0])
    .filter { (policy, password) ->
        val occurrences = password.count { it == policy.char }
        policy.min <= occurrences && policy.max >= occurrences
    }.toList().size

println(result)
