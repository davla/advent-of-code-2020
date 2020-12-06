package advent.of.code

import advent.of.code.util.*

object Day1 {
    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        (inputLines.map(String::toInt)
            .asIterable()
            .let { it.cartesianProduct(it) }
            .products.find { it.sum() == 2020 }
            ?: throw IllegalArgumentException())
            .let { it.map(Int::toLong).reduce(Long::times) }
}
