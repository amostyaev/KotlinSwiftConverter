package com.raistlin.converter.parser

import com.raistlin.converter.base.*
import java.io.File

abstract class Parser(private val source: String) {

    fun parseFile(): MutableList<Lexeme> {
        val result = mutableListOf<Lexeme>()
        val text = File(source).reader().readText()
        var index = 0
        while (index < text.length) {
            val divider = dividerRegex.find(text, index)
            val lex = if (divider != null) text.substring(index, divider.range.first) else text.substring(index)
            if (eolRegex.find(lex) != null) {
                result.add(DelimiterLexeme(Delimiter.EOL))
            } else if (lex.trim().isNotEmpty()) {
                result.add(formLexeme(lex))
            }
            if (divider == null) break

            val substring = text.substring(divider.range.first)
            val delimiter = Delimiter.values().find { substring.startsWith(it.kotlinValue) }
            index = when (delimiter) {
                null -> divider.range.first + 1
                Delimiter.CommentLine -> {
                    val commentStart = divider.range.first + delimiterValue(delimiter).length
                    val eol = eolRegex.find(text, commentStart)
                    if (eol != null) {
                        result.add(DelimiterLexeme(Delimiter.CommentLine))
                        result.add(ConstantLexeme(text.substring(commentStart, eol.range.first), ConstantType.AsIs))
                    }
                    eol?.range?.last ?: text.length
                }
                Delimiter.CommentBlockStart -> {
                    val commentStart = divider.range.first + delimiterValue(delimiter).length
                    val blockEnd = blockEndRegex.find(text, commentStart)
                    if (blockEnd != null) {
                        result.add(DelimiterLexeme(Delimiter.CommentBlockStart))
                        result.add(
                            ConstantLexeme(
                                text.substring(commentStart, blockEnd.range.first),
                                ConstantType.AsIs
                            )
                        )
                        result.add(DelimiterLexeme(Delimiter.CommentBlockEnd))
                    }
                    (blockEnd?.range?.last ?: text.length) + 1
                }
                Delimiter.Quote -> {
                    val stringEnd = stringEndRegex.find(text, divider.range.last + 1)
                    if (stringEnd != null) {
                        result.add(
                            ConstantLexeme(
                                text.substring(divider.range.last + 1, stringEnd.range.first),
                                ConstantType.String
                            )
                        )
                    }
                    (stringEnd?.range?.last ?: text.length) + 1
                }
                else -> {
                    result.add(DelimiterLexeme(delimiter))
                    divider.range.first + delimiterValue(delimiter).length
                }
            }
        }
        return result
    }

    private fun formLexeme(value: String): Lexeme {
        val keyword = Keyword.values().find { value == keywordValue(it) }
        return if (keyword != null) KeywordLexeme(keyword) else IdentifierLexeme(value)
    }

    abstract fun delimiterValue(delimiter: Delimiter): String

    abstract fun keywordValue(keyword: Keyword): String
}

class KotlinParser(source: String) : Parser(source) {

    override fun delimiterValue(delimiter: Delimiter) = delimiter.kotlinValue

    override fun keywordValue(keyword: Keyword): String = keyword.kotlinValue

}

class SwiftParser(source: String) : Parser(source) {

    override fun delimiterValue(delimiter: Delimiter) = delimiter.swiftValue

    override fun keywordValue(keyword: Keyword): String = keyword.swiftValue

}