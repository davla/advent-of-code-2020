package advent.of.code

import kotlin.math.pow

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

private data class Mask(val bits: String) : Instruction()

private data class Mem(val address: Long, val value: Long) : Instruction() {
    companion object {
        val addressRegex = "mem\\[(\\d+)\\]".toRegex()
    }
}

object Day14 {
    private abstract class CPU {
        protected lateinit var currentBitMask: Mask
        protected val memory = mutableMapOf<Long, Long>()

        abstract protected fun executeMem(mem: Mem)

        fun execute(instructions: Sequence<Instruction>) {
            instructions.forEach {
                when (it) {
                    is Mask -> currentBitMask = it
                    is Mem -> executeMem(it)
                }
            }
        }

        fun memorySum() = memory.values.sum()
    }

    private class CPU1 : CPU() {
        private fun applyMask(value: Long): Long {
            val orMask = currentBitMask.bits.map {
                when (it) {
                    'X' -> '0'
                    else -> it
                }
            }.joinToString(separator = "").toLong(radix = 2)

            val andMask = currentBitMask.bits.map {
                when (it) {
                    'X' -> '1'
                    else -> it
                }
            }.joinToString(separator = "").toLong(radix = 2)

            return orMask or (andMask and value)
        }

        override protected fun executeMem(mem: Mem) {
            memory.put(mem.address, applyMask(mem.value))
        }
    }

    private class CPU2 : CPU() {
        private fun applyMask(address: Long): Sequence<Long> {
            val addressBits = address.toString(radix = 2)
                .padStart(currentBitMask.bits.length, '0')
            val withX = addressBits.zip(currentBitMask.bits)
                { addressBit, maskBit ->
                    when (maskBit) {
                        '0' -> addressBit
                        else -> maskBit
                    }
                }
            return expandX(withX.joinToString(separator = ""))
        }

        override protected fun executeMem(mem: Mem) {
            applyMask(mem.address).forEach { address ->
                memory.put(address, mem.value)
            }
        }

        companion object {
            private fun expandX(mask: String): Sequence<Long> {
                val xIndices = mask.withIndex()
                    .filter { it.value == 'X' }
                    .map { it.index }
                val xCount = xIndices.size
                val xBits = (0..2.0.pow(xCount).toLong())
                    .asSequence()
                    .map { it.toString(radix = 2).padStart(xCount, '0') }
                return xBits.map {
                        xIndices.zip(it.asIterable()).fold(mask)
                            { acc, (maskIndex, xBit) ->
                                acc.replaceRange(maskIndex, maskIndex + 1,
                                    xBit.toString())
                            }
                    }
                    .map { it.toLong(radix = 2) }
            }
        }
    }

    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>) =
        CPU1()
            .apply { execute(inputLines.map(Instruction::parse)) }
            .memorySum()

    fun puzzle2(inputLines: Sequence<String>, args: Collection<String>) =
        CPU2()
            .apply { execute(inputLines.map(Instruction::parse)) }
            .memorySum()
}
