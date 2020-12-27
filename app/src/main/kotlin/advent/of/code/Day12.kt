package advent.of.code

import kotlin.math.abs

private typealias Coords = Pair<Int, Int>

private sealed class MoveDirection(val advance: (Coords, Int) -> Coords)
private object North : MoveDirection({ (x, y), distance -> Coords(x, y - distance) })
private object East : MoveDirection({ (x, y), distance -> Coords(x + distance, y) })
private object South : MoveDirection({ (x, y), distance -> Coords(x, y + distance) })
private object West : MoveDirection({ (x, y), distance -> Coords(x - distance, y) })

private sealed class TurnDirection(private val loop: Collection<MoveDirection>) {
    fun turn(start: MoveDirection, value: Int): MoveDirection {
        if (value % 90 != 0) {
            throw IllegalArgumentException()
        }

        val startIndex = loop.indexOf(start)
            .also { if (it == -1) throw IllegalArgumentException() }
        val targetIndex = (startIndex + value / 90) % loop.count()
        return loop.elementAt(targetIndex)
    }
}
private object Left : TurnDirection(listOf(North, West, South, East))
private object Right : TurnDirection(listOf(North, East, South, West))

private sealed class Action(val distance: Int) {
    companion object {
        fun parse(line: String): Action {
            val type = line[0]
            val value = line.substring(1).toInt()
            return when (type) {
                'N' -> MoveAction(North, value)
                'E' -> MoveAction(East, value)
                'S' -> MoveAction(South, value)
                'W' -> MoveAction(West, value)
                'F' -> Forward(value)
                'L' -> TurnAction(Left, value)
                'R' -> TurnAction(Right, value)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
private class MoveAction(val direction: MoveDirection, distance: Int)
        : Action(distance) {
    fun move(start: Coords) = direction.advance(start, distance)
}
private class TurnAction(val direction: TurnDirection, distance: Int)
        : Action(distance) {
    fun turn(start: MoveDirection) = direction.turn(start, distance)
}
private class Forward(distance: Int) : Action(distance)

object Day12 {
    private class Ship(private var moveDirection: MoveDirection = East) {
        private var pos = Coords(0, 0)

        fun manhattanDistance() = abs(pos.first) + abs(pos.second)

        fun execute(actions: Sequence<Action>) {
            actions.forEach {
                when (it) {
                    is MoveAction -> pos = it.move(pos)
                    is TurnAction -> moveDirection = it.turn(moveDirection)
                    is Forward -> pos = moveDirection.advance(pos, it.distance)
                }
            }
        }
    }

    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>) =
        Ship()
            .apply { execute(inputLines.map(Action::parse)) }
            .manhattanDistance()
}
