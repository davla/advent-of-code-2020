package advent.of.code

import advent.of.code.util.*

object Day1 {
    private fun findAndMultiply(products: Iterable<Iterable<Int>>) =
        (products.find { it.sum() == 2020 }
            ?: throw IllegalArgumentException())
            .let { it.map(Int::toLong).reduce(Long::times) }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        findAndMultiply(inputLines.map(String::toInt)
            .asIterable()
            .let { it.cartesianProduct(it) })

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        findAndMultiply(inputLines.map(String::toInt)
            .asIterable()
            .let { it.cartesianProduct(it, it) })
}
