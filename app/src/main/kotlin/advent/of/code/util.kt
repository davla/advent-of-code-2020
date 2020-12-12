package advent.of.code.util

fun <T> Iterable<T>.cartesianProduct(vararg others: Iterable<T>): Iterable<Iterable<T>> =
    others.fold(this.map(::listOf)) { acc, ts ->
        acc.flatMap { xs -> ts.map { y -> xs + listOf(y) } }
    }

fun <T> Sequence<T>.split(predicate: (T) -> Boolean): Iterable<Iterable<T>> =
    fold(mutableListOf(mutableListOf<T>())) { acc, item ->
        if (predicate(item))
            acc.add(mutableListOf<T>())
        else
            acc.last().add(item)
        acc
    }

fun <T> Iterable<T>.partitionWhile(predicate: (T) -> Boolean): Pair<Iterable<T>, Iterable<T>> =
    Pair(takeWhile(predicate), dropWhile(predicate))

fun <T, U> Pair<T, U>.asIterable() = listOf(first, second)

fun IntProgression.lowerHalf() = first..(first + count() / 2 - 1)
fun IntProgression.upperHalf() = (last - count() / 2 + 1)..last

class Matrix<T>(private val items: Collection<Collection<T>>) {
    data class Indices(val row: Int, val col: Int)

    val size = Pair(items.size, items.first().size)

    override operator fun equals(other: Any?) =
        other is Matrix<*> && this.items == other.items

    override fun toString() =
        items.map { it.joinToString(separator = "") }
            .joinToString(separator = "\n")

    fun colAt(at: Int) = items.map { it.elementAt(at) }

    fun count(predicate: (T) -> Boolean) =
        items.fold(0) { count, row -> count + row.count(predicate) }

    fun elementAt(row: Int, col: Int) =
        items.elementAt(row).elementAt(col)

    fun elementAtOrNull(row: Int, col: Int) =
        items.elementAtOrNull(row)?.elementAtOrNull(col)

    fun <R> mapIndexed(f: (Indices, T) -> R) =
        Matrix(items.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, item ->
                f(Indices(rowIndex, colIndex), item)
            }
        })

    fun rowAt(at: Int) = items.elementAt(at)

    fun slice(rowIndices: IntProgression, colIndices: IntProgression) =
        rowIndices.zip(colIndices, ::elementAt)
}
