package com.raistlin.converter.formatter

import com.raistlin.converter.base.*
import java.lang.StringBuilder

class Formatter {

    fun formatLexemes(lexemes: List<Lexeme>): String {
        val sb = StringBuilder()
        for ((index, lexeme) in lexemes.withIndex()) {
            when (lexeme) {
                is KeywordLexeme -> print(lexeme.keyword.swiftValue)
                is DelimiterLexeme -> print(lexeme.delimiter.swiftValue)
                is IdentifierLexeme -> print(lexeme.identifier)
                is ConstantLexeme -> print(lexeme.formattedValue)
            }
            if (needSpaceAfter(lexeme, lexemes.getOrNull(index + 1))) {
                print(" ")
            }
        }
        return sb.toString()
    }

    private fun needSpaceAfter(lexeme: Lexeme, lexemeAfter: Lexeme?): Boolean {
        if (lexeme.isDelimiter(Delimiter.EOL)
            || lexeme.isDelimiter(Delimiter.Dot)
            || lexeme.isDelimiter(Delimiter.Indent)
            || lexeme.isDelimiter(Delimiter.OpenRoundBracket)
            || lexeme.isDelimiter(Delimiter.OpenSquareBracket)
            || lexeme.isDelimiter(Delimiter.OpenAngleBracket)
            || lexeme.isDelimiter(Delimiter.CommentLine)
            || lexeme.isDelimiter(Delimiter.CommentBlockStart)
        ) {
            return false
        }

        if (lexeme is IdentifierLexeme && (lexemeAfter?.isDelimiter(Delimiter.OpenRoundBracket) == true
                    || lexemeAfter?.isDelimiter(Delimiter.OpenSquareBracket) == true
                    || lexemeAfter?.isDelimiter(Delimiter.OpenAngleBracket) == true)
        ) {
            return false
        }

        if (lexemeAfter?.isDelimiter(Delimiter.Comma) == true
            || lexemeAfter?.isDelimiter(Delimiter.Colon) == true
            || lexemeAfter?.isDelimiter(Delimiter.Dot) == true
            || lexemeAfter?.isDelimiter(Delimiter.Question) == true
            || lexemeAfter?.isDelimiter(Delimiter.Exclamation) == true
            || lexemeAfter?.isDelimiter(Delimiter.CloseRoundBracket) == true
            || lexemeAfter?.isDelimiter(Delimiter.CloseSquareBracket) == true
            || lexemeAfter?.isDelimiter(Delimiter.CloseAngleBracket) == true
        ) {
            return false
        }

        return true
    }

}