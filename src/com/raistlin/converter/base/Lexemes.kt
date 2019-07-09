package com.raistlin.converter.base

abstract class Lexeme

class DelimiterLexeme(val delimiter: Delimiter) : Lexeme() {

    override fun toString(): String {
        return "Delimiter $delimiter"
    }
}

class KeywordLexeme(val keyword: Keyword) : Lexeme() {

    override fun toString(): String {
        return "Keyword $keyword"
    }
}

class IdentifierLexeme(val identifier: String) : Lexeme() {

    override fun toString(): String {
        return "Identifier $identifier"
    }
}

enum class ConstantType {
    Number, String, AsIs
}

class ConstantLexeme(private val value: String, private val type: ConstantType) : Lexeme() {

    val formattedValue: String
        get() {
            return when (type) {
                ConstantType.Number -> value
                ConstantType.String -> "\"$value\""
                ConstantType.AsIs -> value
            }
        }

    override fun toString(): String {
        return "Constant $value"
    }
}

fun Lexeme.isDelimiter(delimiter: Delimiter) = this is DelimiterLexeme && this.delimiter == delimiter

fun Lexeme.isKeyword(keyword: Keyword) = this is KeywordLexeme && this.keyword == keyword