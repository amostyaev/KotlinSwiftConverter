package com.raistlin.converter

import com.raistlin.converter.formatter.Formatter
import com.raistlin.converter.parser.KotlinParser

fun main() {
    val parser = KotlinParser("scratch.txt")
    val lexemes = parser.parseFile()
    val formatter = Formatter()
    val result = formatter.formatLexemes(lexemes)
    print(result)
}