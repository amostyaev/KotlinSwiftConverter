package com.raistlin.converter.parser

val dividerRegex = "[\\s-+*/=><!.?,:(){}\\[\\]\"]".toRegex()
val eolRegex = "(\\r\\n|\\n|\\r)".toRegex()
val blockEndRegex = "\\*/".toRegex()
val stringEndRegex = "\"".toRegex()