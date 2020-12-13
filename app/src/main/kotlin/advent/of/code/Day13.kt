package advent.of.code

import kotlin.math.ceil

object Day13 {
    private fun departuresAt(now: Long, buses: Iterable<Pair<Long, Long>>) =
        buses.map { (offset, departureInterval) ->
            val nextDeparture = (ceil(now.toDouble() / departureInterval).toLong()
                * departureInterval)
            Pair(offset, nextDeparture - now)
        }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>): Long {
        val arrivalTime = inputLines.first().toLong()
        val buses = inputLines.drop(1).first()
            .split(",")
            .filter { it != "x" }
            .map(String::toLong)
        return (buses.map { Pair(it, (arrivalTime / it + 1) * it - arrivalTime) }
            .minByOrNull { it.second }
            ?: throw IllegalArgumentException())
            .let { it.first * it.second }
    }

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        (inputLines.drop(1).first()
            .split(",")
            .withIndex()
            .filter { it.value != "x" }
            .map { (index, value) -> Pair(index.toLong(), value.toLong()) }
            .let { buses ->
                val minInterval = buses.map { it.second }.minOrNull()
                    ?: throw IllegalArgumentException()
                generateSequence(0L) { it + minInterval }
                    .map { time -> Pair(time, departuresAt(time, buses)) }
            }.find { (_, departures) -> departures.all { it.first == it.second } }
            ?: throw IllegalArgumentException())
            .first
}
