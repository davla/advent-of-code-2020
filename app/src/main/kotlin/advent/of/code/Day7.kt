package advent.of.code

import advent.of.code.util.*

object Day7 {
    private data class Node(val color: String) {
        companion object {
            fun parse(tokens: Iterable<String>) =
                Node(tokens.takeWhile { !it.startsWith("bag") }
                    .joinToString(separator = " "))

            fun parseWithCount(tokens: Iterable<String>): Pair<Node, Int>? =
                if (tokens.joinToString(separator = " ").contains("no other"))
                    null
                else
                    Pair(Node.parse(tokens.drop(1)), tokens.first().toInt())
        }
    }

    private data class Edge(val container: Node, val content: Pair<Node, Int>)

    private class Graph {
        private val nodes = mutableSetOf<Node>()
        private val edges = mutableSetOf<Edge>()

        fun addNode(node: Node) { nodes.add(node) }
        fun addEdge(edge: Edge) { edges.add(edge) }

        fun countContent(node: Node): Int =
            1 + edges.filter { it.container == node }
                .map(Edge::content)
                .sumBy { (node, quantity) -> quantity * countContent(node) }

        fun findContainers(node: Node): Set<Node> =
            edges.filter { it.content.first == node }
                .map(Edge::container)
                .toSet()
                .let { it + it.flatMap(::findContainers) }

        companion object {
            fun parse(inputLines: Sequence<String>) =
                inputLines.fold(Graph()) { graph, line ->
                    val tokens = line.split(" ")
                    val (container, content) = tokens.partitionWhile { !it.contains("contain") }

                    val parentNode = Node.parse(container)
                    val childNodes = content.drop(1)
                        .asSequence().split { it.contains(",") }
                        .map(Node::parseWithCount)
                        .filterNotNull()

                    graph.addNode(parentNode)
                    childNodes.forEach {
                        graph.addNode(it.first)
                        graph.addEdge(Edge(parentNode, it))
                    }

                    graph
                }
        }
    }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        Graph.parse(inputLines)
            .findContainers(Node("shiny gold"))
            .size

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>) =
        Graph.parse(inputLines)
            .countContent(Node("shiny gold")) - 1
}
