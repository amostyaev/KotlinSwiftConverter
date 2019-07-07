package com.raistlin.converter.base

enum class Delimiter(val kotlinValue: String, val swiftValue: String = kotlinValue) {

    // three char delimiters
    EqualStrong("==="),

    // two char delimiters
    Equal("=="),
    EqualGreater(">="),
    EqualLower("<="),
    NotEqual("!="),

    AddSelf("+="),
    SubSelf("-="),
    MulSelf("*="),
    DivSelf("/="),
    AddSelfUnary("++"),
    SubSelfUnary("--"),

    Elvis("?:", "??"),
    NotNull("!!"),
    CaseLine("->", "case"),

    CommentLine("//"),
    CommentBlockStart("/*"),
    CommentBlockEnd("*/"),

    // single char delimiters
    Assign("="),
    Quote("\""),

    Add("+"),
    Sub("-"),
    Mul("*"),
    Div("/"),

    Colon(":"),
    Comma(","),
    Dot("."),
    Semicolon(";"),
    Question("?"),
    Exclamation("!"),

    OpenRoundBracket("("),
    CloseRoundBracket(")"),
    OpenCurlyBracket("{"),
    CloseCurlyBracket("}"),
    OpenSquareBracket("["),
    CloseSquareBracket("]"),
    OpenAngleBracket("<"),
    CloseAngleBracket(">"),

    // special
    Indent("    "),
    EOL("\n")
}