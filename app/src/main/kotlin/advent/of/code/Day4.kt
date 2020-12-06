package advent.of.code

import advent.of.code.util.*

object Day4 {
    private class Passport(val birthYear: Int?,
                           val issueYear: Int?,
                           val expirationYear: Int?,
                           val height: Pair<Int, String>?,
                           val hairColor: String?,
                           val eyeColor: String?,
                           val passportID: String?,
                           val countryID: Int?) {
        val isValid = birthYear != null
            && issueYear != null
            && expirationYear != null
            && height != null
            && hairColor != null
            && eyeColor != null
            && passportID != null

        companion object {
            val fieldSeparator = "\\s+".toRegex()

            private fun makeHeight(value: String): Pair<Int, String> {
                val (number, unitOfMeasure) = value.asIterable()
                    .partitionWhile(Char::isDigit)
                return Pair(
                    number.joinToString(separator = "").toInt(),
                    unitOfMeasure.joinToString(separator = "")
                )
            }

            fun parse(pairs: Iterable<String>) =
                pairs.map {
                        val keyVal = it.split(":")
                        Pair(keyVal[0], keyVal[1])
                    }.toMap()
                    .let {
                        Passport(it.get("byr")?.toInt(),
                                 it.get("iyr")?.toInt(),
                                 it.get("eyr")?.toInt(),
                                 it.get("hgt")?.let(::makeHeight),
                                 it.get("hcl"),
                                 it.get("ecl"),
                                 it.get("pid"),
                                 it.get("cid")?.toInt()
                        )
                    }
        }
    }

    private class StrictPassport(private val passport: Passport) {
        val isValid = passport.isValid
            && passport.birthYear.toString().length == 4
            && passport.birthYear in 1920..2002
            && passport.issueYear.toString().length == 4
            && passport.issueYear in 2010..2020
            && passport.expirationYear.toString().length == 4
            && passport.expirationYear in 2020..2030
            && when (passport.height!!.second) {
                "cm" -> passport.height.first in 150..193
                "in" -> passport.height.first in 59..76
                else -> false
            }
            && passport.hairColor!!.length == 7
            && passport.hairColor.startsWith("#")
            && passport.hairColor.drop(1).toIntOrNull(radix = 16) != null
            && passport.eyeColor in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            && passport.passportID!!.length == 9
            && passport.passportID.toIntOrNull() != null
    }

    private fun parseInput(lines: Sequence<String>) =
        lines.split(String::isBlank)
            .map { it.flatMap { it.split(Passport.fieldSeparator) } }
            .map(Passport::parse)

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        parseInput(inputLines)
            .count(Passport::isValid)

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        parseInput(inputLines)
            .map(::StrictPassport)
            .count(StrictPassport::isValid)

}
