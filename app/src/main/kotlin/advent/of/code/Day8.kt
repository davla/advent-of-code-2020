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

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.map { it.split(" ") }
            .map(Instruction::parse)
            .toList()
            .let { CPU().apply { execute(it) }.acc }

    fun puzzle2(inputLines: Sequence<String>, args: Iterable<String>): Int {
        // val instructionsToChange = program.count { (op, _) -> op in listOf("nop", "jmp") }
        var counter = 0

        var cpu: CPU = CPU()
        try {
            var lastIndex = 0
            while (true) {
                        val program = inputLines.map { it.split(" ") }
            .map(Instruction::parse)
            .toList()

                counter += 1
                println("Progress: ${counter.toDouble() / 290 * 100}%")

                cpu = CPU()
                lastIndex = (program.withIndex().toList().subList(lastIndex, program.size)
                    .find { (_, v) -> v.op in listOf("nop", "jmp") } ?: throw NullPointerException())
                    .index

                // lastIndex = program.subList(lastIndex, program.size)
                //     .indexOfFirst { it.op in listOf("nop", "jmp") } + lastIndex
                println(lastIndex)
                val instruction = program.elementAt(lastIndex)
                val alteredProgram = (program.take(lastIndex)
                    + listOf(Instruction(when (instruction.op) {
                        "nop" -> "jmp"
                        "jmp" -> "nop"
                        else -> instruction.op}, instruction.arg))
                    + program.drop(lastIndex + 1))
                lastIndex += 1

                cpu.execute(alteredProgram)
            }
        } catch (e: IllegalArgumentException) {
            return cpu.acc
        }
    }
}
