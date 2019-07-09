package com.raistlin.converter

import com.raistlin.converter.formatter.Formatter
import com.raistlin.converter.marker.Marker
import com.raistlin.converter.parser.KotlinParser

fun main() {
    val parser = KotlinParser("scratch.txt")
    val lexemes = parser.parseFile()
    val formatter = Formatter()
    //print(formatter.formatLexemes(lexemes))
    val marker = Marker(lexemes)
    val nodes = marker.groupLexemes()
    print(formatter.formatNodes(nodes))
}