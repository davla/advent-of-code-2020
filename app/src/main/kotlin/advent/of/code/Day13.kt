package advent.of.code

object Day13 {
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

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) = 45
}
