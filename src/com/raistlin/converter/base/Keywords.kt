package com.raistlin.converter.base

enum class Keyword(val kotlinValue: String, val swiftValue: String = kotlinValue) {

    Null("null", "nil"),

    Var("var"),
    Val("val", "let"),

    If("if"),
    Else("else"),
    For("for"),
    In("in"),
    Do("do"),
    While("while"),
    Break("break"),
    Continue("continue"),
    When("when", "switch"),
    // case is a delimiter

    Is("is"),
    As("as"),

    Fun("fun", "func"),
    Class("class"),

}