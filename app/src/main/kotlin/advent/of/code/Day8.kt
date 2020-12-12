package advent.of.code

object Day8 {
    private class Instruction(val op: String, val arg: Int) {
        companion object {
            fun parse(tokens: Iterable<String>) =
                Instruction(tokens.first(), tokens.drop(1).first().toInt())
        }
    }

    private class CPU {
        var acc = 0
        var programCounter = 0

        private val visitedAddresses = mutableSetOf<Int>()

        fun execute(program: Collection<Instruction>) {
            while (programCounter < program.count()) {
                val instruction = program.elementAt(programCounter)
                if (programCounter in visitedAddresses) {
                    return
                }
                visitedAddresses.add(programCounter)

                when (instruction.op) {
                    "acc" -> acc += instruction.arg
                }
                when (instruction.op) {
                    "jmp" -> programCounter += instruction.arg
                    else -> programCounter += 1
                }
            }
        }
    }

    private class ControlFlowGraph {
        class Node(val instruction: Instruction, val address: Int)
        class Edge(val from: Node, val to: Node) {
            private val distance = to.address - from.address
            val isNopFix = from.op == "nop" && distance != 1
            val isJumpFix = from.op == "jmp" && distance != from.instruction.arg
        }

        val nodes = mutableSetOf<Node>()
        val edges = mutableSetOf<Edge>()

        val edgesWithNoTo = mutableSetOf<Pair<Node, Int>>()

        fun addNode(node: Node) {
            nodes.add(node)

            val from = edgesWithNoTo.any { it.second == node.address }
            if (from != null) {
                addEdge(from.first, node)
            }
        }

        fun addEdge(from: Node, to: Node) { edges.add(Edge(from, to)) }

        fun addEdge(from: Node, offset: Int) {
            val targetAddress = from.address + offset
            val target = nodes.find { it.address == targetAddress }
            if (target != null) {
                addEdge(from, target)
            }
            else {
                edgesWithNoTo.add(Pair(from, targetAddress))
            }
        }

        fun addEdge(from: Node) { addEdge(from, from.address.arg) }
    }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.map { it.split(" ") }
            .map(Instruction::parse)
            .toList()
            .let { CPU().apply { execute(it) }.acc }

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>): Int {
        inputLines.map { it.split(" ") }
            .map(Instruction::parse)
            .foldIndexed(ControlFlowGraph()) { address, graph, instruction ->
                val fromNode = Node(instruction, addresss)
                graph.addNode(fromNode)
                graph.addEdge(fromNode, 1)

                when (instruction.op) {
                    "nop", "jmp" -> graph.addEdge(fromNode)
                    else -> Unit
                }

                graph
            }
    }
}
