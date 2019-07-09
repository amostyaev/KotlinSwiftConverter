package com.raistlin.converter.marker

import com.raistlin.converter.base.*

class Marker(private val lexemes: List<Lexeme>) {

    private var index = 0

    fun groupLexemes(): ModuleNode {
        return groupModuleNode()
    }

    private fun groupModuleNode(): ModuleNode {
        val result = ModuleNode()
        index = 0
        var textStart: Int = index
        while (index < lexemes.size) {
            if (lexemes[index].isKeyword(Keyword.Fun)) {
                if (index != textStart) {
                    result.addChildNode(groupTextNode(textStart, index))
                }
                result.addChildNode(groupFunctionNode())
                nextValuableLex()
                textStart = index
            } else if (lexemes[index].isKeyword(Keyword.Class)) {
                if (index != textStart) {
                    result.addChildNode(groupTextNode(textStart, index))
                }
                result.addChildNode(groupClassNode())
                nextValuableLex()
                textStart = index
            } else {
                nextValuableLex()
            }
        }
        if (index != textStart) {
            result.addChildNode(groupTextNode(textStart, index))
        }
        return result
    }

    private fun groupTextNode(start: Int, end: Int): TextNode {
        return TextNode(lexemes.subList(start, end))
    }

    private fun groupFunctionNode(): FunctionNode {
        val startIndex = index++ // skip fun
        val nameLex = lexemes.getOrNull(index)
        val name = if (nameLex is IdentifierLexeme) nameLex.identifier else ""
        while (index < lexemes.size
            && !lexemes[index].isDelimiter(Delimiter.OpenCurlyBracket) && !lexemes[index].isDelimiter(Delimiter.Assign)
        ) {
            ++index
        }
        val bodyStart = index
        if (lexemes[index].isDelimiter(Delimiter.OpenCurlyBracket)) {
            ++index
            var rang = 1
            while (index < lexemes.size && rang != 0) {
                if (lexemes[index].isDelimiter(Delimiter.OpenCurlyBracket)) {
                    ++rang
                } else if (lexemes[index].isDelimiter(Delimiter.CloseCurlyBracket)) {
                    --rang
                }
                ++index
            }
        } else {
            // one-line function
            while (index < lexemes.size && !lexemes[index].isDelimiter(Delimiter.EOL)) {
                ++index
            }
            ++index
        }
        return FunctionNode(name, groupTextNode(startIndex, bodyStart), groupTextNode(bodyStart + 1, index - 1))
    }

    private fun groupClassNode(): ClassNode {
        val properties = mutableListOf<TextNode>()
        val methods = mutableListOf<FunctionNode>()
        val startIndex = index++ // skip class
        val nameLex = lexemes.getOrNull(index)
        val name = if (nameLex is IdentifierLexeme) nameLex.identifier else ""
        var rang = 0
        while (index < lexemes.size) {
            if (rang == 0 && (lexemes[index].isDelimiter(Delimiter.OpenCurlyBracket) || lexemes[index] is KeywordLexeme))
                break
            if (lexemes[index].isDelimiter(Delimiter.OpenRoundBracket)) {
                rang++
            } else if (lexemes[index].isDelimiter(Delimiter.CloseRoundBracket)) {
                rang--
            }
            ++index
        }
        val bodyStart = index
        val specification = groupTextNode(startIndex, bodyStart)
        if (index < lexemes.size && lexemes[index].isDelimiter(Delimiter.OpenCurlyBracket)) {
            ++index
            var textStart = index
            while (index < lexemes.size && !lexemes[index].isDelimiter(Delimiter.CloseCurlyBracket)) {
                if (lexemes[index].isKeyword(Keyword.Fun)) {
                    if (index != textStart) {
                        properties.add(groupTextNode(textStart, index))
                    }
                    methods.add(groupFunctionNode())
                    nextValuableLex()
                    textStart = index
                } else {
                    nextValuableLex()
                }
            }
            if (index != textStart) {
                properties.add(groupTextNode(textStart, index))
            }
        } else {
            --index // revert next keyword back
        }
        return ClassNode(name, specification, properties, methods)
    }

    private fun nextValuableLex() {
        ++index
        while (index < lexemes.size && (lexemes[index].isDelimiter(Delimiter.EOL)
                    || lexemes[index].isDelimiter(Delimiter.Indent))
        ) {
            ++index
        }
    }
}