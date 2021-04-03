package advent.of.code

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

private sealed class ArithmeticExp {
    abstract fun evaluate(): Long
}

private data class Number(val n: Long) : ArithmeticExp() {
    override fun evaluate() = n
}
private data class Addition(
    val left: ArithmeticExp,
    val right: ArithmeticExp
): ArithmeticExp() {
    override fun evaluate(): Long = left.evaluate() + right.evaluate()
}
private data class Multiplication(
    val left: ArithmeticExp,
    val right: ArithmeticExp
): ArithmeticExp() {
    override fun evaluate(): Long = left.evaluate() * right.evaluate()
}

object Day18 {
    private val grammar = object : Grammar<ArithmeticExp>() {
        val openParen by literalToken("(")
        val closeParen by literalToken(")")
        val plus by literalToken("+")
        val times by literalToken("*")
        val number by regexToken("-?\\d+")
        val whitespaces by regexToken("\\s+", ignore = true)

        val parenExp by skip(openParen) * parser(::exp) * skip(closeParen)
        val term by
            parenExp or
            number.map { Number(it.text.toLong()) }

        val binaryOpTokens by plus or times
        val binaryOp by leftAssociative(term, binaryOpTokens) { a, op, b ->
            when (op.type) {
                plus -> Addition(a, b)
                times -> Multiplication(a, b)
                else -> throw IllegalArgumentException()
            }
        }

        val exp: Parser<ArithmeticExp> by
            binaryOp or
            term

        override val rootParser by exp
    }

    fun puzzle1(inputLines: Sequence<String>, args: Collection<String>): Long =
        inputLines.map(grammar::parseToEnd)
            .map(ArithmeticExp::evaluate)
            .sum()
}
