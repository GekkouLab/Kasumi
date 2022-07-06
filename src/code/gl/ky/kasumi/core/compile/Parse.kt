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

    private fun parseModule(s: ParseState): KNode.Module = with(s) {
        val functions = arrayListOf<KNode.Function>()
        while (hasNext()) {
            functions.add(parseFunction(this))
        }
        return KNode.Module(functions)
    }

    private fun parseFunction(s: ParseState): KNode.Function = with(s) {
        val name = expect(KasumiTokenTypes.STRING).content
        val body = parseBlock(this)
        KNode.Function(name, body)
    }

    private fun parseBlock(s: ParseState): KNode.Block = with(s) {
        expect(KasumiTokenTypes.COLON)
        val statements = arrayListOf<KNode.Statement>()
        while (!test(KasumiTokenTypes.SEMICOLON)) {
            statements.add(parseStatement(this))
        }
        expect(KasumiTokenTypes.SEMICOLON)
        KNode.Block(statements)
    }

    private fun parseStatement(s: ParseState): KNode.Statement = with(s) {
        when {
            test(KasumiTokenTypes.COLON) -> parseBlock(this)
            else -> throw RuntimeException()
        }
    }

    private fun parseVarAssign(s: ParseState): KNode.VarAssign = with(s) {
        expect(KasumiTokenTypes.VAR)
        val name = expect(KasumiTokenTypes.STRING).content
        expect(KasumiTokenTypes.AS)
        val value = parseExpression(this)
        KNode.VarAssign(name, value)
    }

    private fun parseCommand(s: ParseState): KNode.Command = with(s) {
        TODO()
    }

    private fun parseExpression(s: ParseState): KNode.Expression = with(s) {
        TODO()
    }

}