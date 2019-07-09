package com.raistlin.converter.base

abstract class SyntaxNode

class ModuleNode {

    val childNodes = mutableListOf<SyntaxNode>()

    fun addChildNode(node: SyntaxNode) = childNodes.add(node)

}

class TextNode(val lexemes: List<Lexeme>) : SyntaxNode()

class FunctionNode(val name: String, val specification: TextNode, val body: TextNode) : SyntaxNode()

class ClassNode(
    val name: String,
    val specification: TextNode,
    val properties: List<TextNode>,
    val methods: List<FunctionNode>
) : SyntaxNode()