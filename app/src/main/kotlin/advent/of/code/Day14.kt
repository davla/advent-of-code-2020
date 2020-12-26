package advent.of.code

private sealed class Instruction {
    companion object {
        fun parse(line: String): Instruction {
            val tokens = line.split(" ")
            val operation = tokens.first()
            val operand = tokens.last()

            if (operation == "mask") {
                return Mask(operand)
            }

            if (operation.startsWith("mem")) {
                val (address) = (Mem.addressRegex.matchEntire(operation)
                    ?: throw IllegalArgumentException()).destructured
                return Mem(address.toLong(), operand.toLong())
            }

            throw IllegalArgumentException()
        }
    }
}

private class Mask(bits: String) : Instruction() {
    val ones = bits.map {
        when (it) {
            'X' -> '0'
            else -> it
        }
    }.joinToString(separator = "").toLong(radix = 2)
    val zeroes = bits.map {
        when (it) {
            'X' -> '1'
            else -> it
        }
    }.joinToString(separator = "").toLong(radix = 2)

    fun apply(n: Long) = ones or (zeroes and n)
}

private data class Mem(val address: Long, val value: Long) : Instruction() {
    companion object {
        val addressRegex = "mem\\[(\\d+)\\]".toRegex()
    }
}

object Day14 {
    private class CPU {
        private lateinit var currentBitMask: Mask
        private val memory = mutableMapOf<Long, Long>()

        fun execute(instructions: Sequence<Instruction>) {
            instructions.forEach {
                when (it) {
                    is Mask -> currentBitMask = it
                    is Mem -> memory.put(it.address, currentBitMask.apply(it.value))
                }
            }
        }

        fun memorySum() = memory.values.sum()
    }

    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>) =
        CPU()
            .apply { execute(inputLines.map(Instruction::parse)) }
            .memorySum()
}
