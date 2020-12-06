package advent.of.code.util

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
