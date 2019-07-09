package com.raistlin.converter.formatter

import com.raistlin.converter.base.*
import java.lang.StringBuilder

class Formatter {

    fun formatNodes(module: ModuleNode): String {
        val sb = StringBuilder()
        for (node in module.childNodes) {
            formatNode(sb, node)
        }
        return sb.toString()
    }

    fun formatLexemes(lexemes: List<Lexeme>): String {
        val sb = StringBuilder()
        for ((index, lexeme) in lexemes.withIndex()) {
            when (lexeme) {
                is KeywordLexeme -> sb.append(lexeme.keyword.swiftValue)
                is DelimiterLexeme -> sb.append(lexeme.delimiter.swiftValue)
                is IdentifierLexeme -> sb.append(lexeme.identifier)
                is ConstantLexeme -> sb.append(lexeme.formattedValue)
            }
            if (needSpaceAfter(lexeme, lexemes.getOrNull(index + 1))) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    private fun formatNode(sb: StringBuilder, node: SyntaxNode) {
        if (node is TextNode) {
            sb.append(formatLexemes(node.lexemes))
        } else if (node is FunctionNode) {
            sb.append("--Function ${node.name}--\n")
            sb.append(formatLexemes(node.specification.lexemes))
            sb.append("{\n")
            sb.append(formatLexemes(node.body.lexemes))
            sb.append("}\n")
            sb.append("--Function ${node.name}--\n")
        } else if (node is ClassNode) {
            sb.append("--Class ${node.name}--\n")
            sb.append(formatLexemes(node.specification.lexemes))
            sb.append("{\n")
            sb.append("--Properties ${node.name}--\n")
            for (property in node.properties) {
                formatNode(sb, property)
            }
            sb.append("--Methods ${node.name}--\n")
            for (method in node.methods) {
                formatNode(sb, method)
            }
            sb.append("}\n")
            sb.append("--Class ${node.name}--\n")
        }
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