package advent.of.code

object Day15 {
    private class MemoryGame(private val seed: List<Long>) {
        private val calledNumbers = seed.dropLast(1)
            .withIndex()
            .associate { Pair(it.value, it.index.toLong() + 1) }
            .toMutableMap()

        private var lastCalled = seed.withIndex()
            .last()
            .let { Pair(it.value, it.index.toLong() + 1) }

        private fun update(nextCalled: Long) {
            val (lastNumber, lastTurn) = lastCalled
            calledNumbers.put(lastNumber, lastTurn)
            lastCalled = Pair(nextCalled, lastTurn + 1)
        }

        fun next() =
            (calledNumbers.get(lastCalled.first)
                ?.let { lastCalled.second - it }
                ?: 0L)
                .also(::update)

        fun asSequence() = seed.asSequence() + generateSequence(::next)
    }

    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>) =
        val seed = inputLines.flatMap { it.split(",").map(String::toLong) }
        return MemoryGame(seed.toList())
            .asSequence()
            .drop(2020 - 1)
            .first()
    }
}
