package advent.of.code

import kotlin.math.abs

private enum class TurnDirection {
    LEFT,
    RIGHT;
}

private enum class MoveDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    fun turn(direction: TurnDirection, value: Int): MoveDirection {
        if (value % 90 != 0) {
            throw IllegalArgumentException()
        }

        val loop = MoveDirection.getTurnSequence(direction)
        val startIndex = loop.indexOf(this)
            .also { if (it == -1) throw IllegalArgumentException() }
        val targetIndex = (startIndex + value / 90) % loop.count()
        return loop.elementAt(targetIndex)
    }

    companion object {
        val ewDirections = listOf(WEST, EAST)
        val nsDirections = listOf(NORTH, SOUTH)

        private fun getTurnSequence(direction: TurnDirection) =
            when (direction) {
                TurnDirection.LEFT -> listOf(NORTH, WEST, SOUTH, EAST)
                TurnDirection.RIGHT -> listOf(NORTH, EAST, SOUTH, WEST)
            }
    }
}

private data class Position
    private constructor(private val x: Int,
                        private val y: Int) {

    val manhattanDistance = abs(x) + abs(y)
    val ew = Pair(if (x > 0) MoveDirection.EAST else MoveDirection.WEST, abs(x))
    val ns = Pair(if (y > 0) MoveDirection.SOUTH else MoveDirection.NORTH, abs(y))

    fun move(direction: MoveDirection, distance: Int) =
        when(direction) {
            MoveDirection.NORTH -> Position(x, y - distance)
            MoveDirection.EAST -> Position(x + distance, y)
            MoveDirection.SOUTH -> Position(x, y + distance)
            MoveDirection.WEST -> Position(x - distance, y)
        }

    fun rotate(direction: TurnDirection, distance: Int): Position {
        val (ewDirection, ewCoord) = ew
        val (nsDirection, nsCoord) = ns
        return Position.fromPairs(
            Pair(ewDirection.turn(direction, distance), ewCoord),
            Pair(nsDirection.turn(direction, distance), nsCoord)
        )
    }

    companion object {
        fun fromPairs(vararg coords: Pair<MoveDirection, Int>): Position {
            val directions = coords.map { it.first }
            if (directions.count { it in MoveDirection.nsDirections } != 1
                || directions.count { it in MoveDirection.ewDirections } != 1) {

                throw IllegalArgumentException()
            }

            val x = coords.find { it.first in MoveDirection.ewDirections }!!
                .let { (direction, value) ->
                    when (direction) {
                        MoveDirection.WEST -> -abs(value)
                        MoveDirection.EAST -> abs(value)
                        else -> throw IllegalArgumentException()
                    }
                }
            val y = coords.find { it.first in MoveDirection.nsDirections }!!
                .let { (direction, value) ->
                    when (direction) {
                        MoveDirection.NORTH -> -abs(value)
                        MoveDirection.SOUTH -> abs(value)
                        else -> throw IllegalArgumentException()
                    }
                }

            return Position(x, y)
        }
    }

}


private sealed class Action(val distance: Int) {
    companion object {
        fun parse(line: String): Action {
            val type = line[0]
            val value = line.substring(1).toInt()
            return when (type) {
                'N' -> MoveAction(MoveDirection.NORTH, value)
                'E' -> MoveAction(MoveDirection.EAST, value)
                'S' -> MoveAction(MoveDirection.SOUTH, value)
                'W' -> MoveAction(MoveDirection.WEST, value)
                'F' -> Forward(value)
                'L' -> TurnAction(TurnDirection.LEFT, value)
                'R' -> TurnAction(TurnDirection.RIGHT, value)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
private class MoveAction(val direction: MoveDirection, distance: Int)
        : Action(distance)
private class TurnAction(val direction: TurnDirection, distance: Int)
        : Action(distance)
private class Forward(distance: Int) : Action(distance)

object Day12 {
    private class Ship(private var pos: Position = Position.fromPairs(
                            Pair(MoveDirection.NORTH, 0),
                            Pair(MoveDirection.WEST, 0)
                       ),
                       private var moveDirection: MoveDirection = MoveDirection.EAST) {

        fun manhattanDistance() = pos.manhattanDistance

        fun execute(actions: Sequence<Action>) {
            actions.forEach {
                when (it) {
                    is MoveAction -> {
                        pos = pos.move(it.direction, it.distance)
                    }
                    is TurnAction -> {
                        moveDirection = moveDirection.turn(it.direction, it.distance)
                    }
                    is Forward -> {
                        pos = pos.move(moveDirection, it.distance)
                    }
                }
            }
        }
    }

    private class ShipWithWayPoint(private var waypointPos: Position = Position.fromPairs(
                                        Pair(MoveDirection.EAST, 10),
                                        Pair(MoveDirection.NORTH, 1)
                                   )) {
        private var shipPos = Position.fromPairs(
            Pair(MoveDirection.EAST, 0),
            Pair(MoveDirection.NORTH, 0)
        )

        fun manhattanDistance() = shipPos.manhattanDistance

        fun execute(actions: Sequence<Action>) {
            actions.forEach {
                when (it) {
                    is MoveAction -> {
                        waypointPos = waypointPos.move(it.direction, it.distance)
                    }
                    is TurnAction -> {
                        waypointPos = waypointPos.rotate(it.direction, it.distance)
                    }
                    is Forward -> {
                        val (ewDirection, ewDistance) = waypointPos.ew
                        val (nsDirection, nsDistance) = waypointPos.ns
                        shipPos = (shipPos.move(ewDirection, ewDistance * it.distance)
                                          .move(nsDirection, nsDistance * it.distance))
                    }
                }
            }
        }
    }

    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>) =
        Ship()
            .apply { execute(inputLines.map(Action::parse)) }
            .manhattanDistance()

    fun puzzle2(inputLines: Sequence<String>, args: Collection<String>) =
        ShipWithWayPoint()
            .apply { execute(inputLines.map(Action::parse)) }
            .manhattanDistance()
}
