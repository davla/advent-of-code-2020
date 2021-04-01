package advent.of.code.util.gameoflife

import advent.of.code.util.cartesianProduct

fun <State> gameOfLife(seed: State, evolve: (State) -> State?) =
    generateSequence(seed, evolve)

class SurroundingEvolution<Position, Element>(
    private val getSurrounding: (Position) -> Iterable<Position>,
    private val evolveElement: (Element, Iterable<Element>) -> Element,
    private val default: Element = throw IllegalArgumentException()
) {
    fun asFunction() = ::evolve
    operator fun invoke(universe: Map<Position, Element>) = evolve(universe)

    fun evolve(universe: Map<Position, Element>): Map<Position, Element> {
        /*
         * We need to call getNeighbors twice. In fact, "first order neighbors"
         * (the initial elements' neighbors) need to evolve too, as they can
         * have neighbors that are not in the default state (the initial
         * elements). Conversely, "second order neighbors" don't need to
         * evolve, since they can only have neighbors in the default state (the
         * "first order neighbors").
         *
         * Therefore, the first call to getNeighbors is to compute which are
         * the "first order neighbors", and it happens only for the initial
         * elements; the second call is to determine the neighbors to use for
         * evolving, and occurs for both the initial element and the
         * "first order neighbors".
         */
        val expandedUniverse = universe.flatMap {
            listOf(it.toPair()) + getNeighbors(universe, it.key).toList()
        }.toMap()
        return expandedUniverse.mapValues { (position, element) ->
            evolveElement(element, getNeighbors(universe, position).values)
        }
    }

    private fun getNeighbors(universe: Map<Position, Element>, position: Position) =
        getSurrounding(position)
            .map { Pair(it, universe.getOrDefault(it, default)) }
            .toMap()
}

data class Indices(val dimensions: Iterable<Long>)
    : Iterable<Long> by dimensions {

    constructor(vararg dimensions: Long) : this(dimensions.toList())

    fun adjacent() = map { (it - 1 .. it + 1) }
        .let { ranges ->
            ranges.first().cartesianProduct(*ranges.drop(1).toTypedArray())
                .map(::Indices)
                .filterNot { it == this }
        }
}
