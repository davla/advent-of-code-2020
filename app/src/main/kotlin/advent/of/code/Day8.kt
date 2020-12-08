package advent.of.code

object Day8 {
    private class Instruction(val op: String, val arg: Int) {
        var execCount = 0

        companion object {
            fun parse(tokens: Iterable<String>) =
                Instruction(tokens.first(), tokens.drop(1).first().toInt())
        }
    }

    private class CPU {
        var acc = 0
        var programCounter = 0

        fun execute(program: Collection<Instruction>) {
            while (programCounter < program.count()) {
                val instruction = program.elementAt(programCounter)
                if (instruction.execCount == 1) {
                    return
                }

                when (instruction.op) {
                    "acc" -> acc += instruction.arg
                }
                when (instruction.op) {
                    "jmp" -> programCounter += instruction.arg
                    else -> programCounter += 1
                }
                instruction.execCount += 1
            }
            throw IllegalArgumentException()
        }
    }

    fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>) =
        inputLines.map { it.split(" ") }
            .map(Instruction::parse)
            .toList()
            .let { CPU().apply { execute(it) }.acc }
}
