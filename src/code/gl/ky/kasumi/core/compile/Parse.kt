package gl.ky.kasumi.core.compile

class ParseState(val tokens: TokenStream, var offset: Int = 0) {
    fun hasNext() = offset < tokens.size
    fun skip(count: Int = 1) {
        offset += count
    }

    fun next() = tokens[offset++]
    fun peek() = tokens[offset]
    fun expect(type: TokenType) = next().also { if (it.type != type) throw RuntimeException() }
    fun test(type: TokenType) = peek().type == type
    fun eat(type: TokenType) {
        if (peek().type == type) next()
    }
}

class Parser {
    fun parse(t: TokenStream) = parseModule(ParseState(t))

    private fun parseModule(s: ParseState): Node.Module = with(s) {
        val functions = arrayListOf<Node.Function>()
        while (hasNext()) {
            functions.add(parseFunction(this))
        }
        return Node.Module(functions)
    }

    private fun parseFunction(s: ParseState): Node.Function = with(s) {
        val name = expect(KasumiTokenTypes.STRING).content
        val body = parseBlock(this)
        Node.Function(name, body)
    }

    private fun parseBlock(s: ParseState): Node.Block = with(s) {
        expect(KasumiTokenTypes.COLON)
        val statements = arrayListOf<Node.Statement>()
        while (!test(KasumiTokenTypes.SEMICOLON)) {
            statements.add(parseStatement(this))
        }
        expect(KasumiTokenTypes.SEMICOLON)
        Node.Block(statements)
    }

    private fun parseStatement(s: ParseState): Node.Statement = with(s) {
        when {
            test(KasumiTokenTypes.COLON) -> parseBlock(this)
            else -> throw RuntimeException()
        }
    }

    private fun parseVarAssign(s: ParseState): Node.VarAssign = with(s) {
        expect(KasumiTokenTypes.VAR)
        val name = expect(KasumiTokenTypes.STRING).content
        expect(KasumiTokenTypes.AS)
        val value = parseExpression(this)
        Node.VarAssign(name, value)
    }

    private fun parseCommand(s: ParseState): Node.Command = with(s) {
        TODO()
    }

    private fun parseExpression(s: ParseState): Node.Expression = with(s) {
        TODO()
    }

}