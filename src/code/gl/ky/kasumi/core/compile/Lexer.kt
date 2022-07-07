package gl.ky.kasumi.core.compile

typealias TokenStream = List<Token>

class Token(val type: TokenType, val content: String)

class TokenType

class LexState(val input: String, var offset: Int) {
    fun skip(n: Int = 1) {
        offset += n
    }
}

interface Lexer {
    fun next(): Token?
    fun expect(type: TokenType): Token
    fun expect(content: String): Token
}

class KLexer(val matches: List<CaptureRule>, val skip: List<CaptureRule>, input: String, offset: Int = 0) : Lexer {
    val state = LexState(input, offset)

    class CaptureRule(val type: TokenType, val regex: Regex)

    override fun next(): Token? {
        val match = Companion.match(state, matches)
        if(match != null) return match
        Companion.skip(state, skip)
        return Companion.match(state, matches)
    }

    override fun expect(type: TokenType): Token {
        val rules = matches.filter { it.type == type }
        val match = Companion.match(state, rules)
        if(match != null) return match
        Companion.skip(state, skip)
        return Companion.match(state, rules) ?: throw RuntimeException("cant found $type")
    }

    override fun expect(content: String): Token {
        if(state.input.startsWith(content, state.offset)) {
            state.skip(content.length)
            return Token(TokenType(), content)
        }
        Companion.skip(state, skip)
        if(state.input.startsWith(content, state.offset)) {
            state.skip(content.length)
            return Token(TokenType(), content)
        }
        throw RuntimeException("cant found $content")
    }

    companion object {
        private fun match(state: LexState, rules: List<CaptureRule>): Token? {
            for (rule in rules) {
                val match = rule.regex.matchAt(state.input, state.offset)
                if (match != null) {
                    state.skip(match.value.length)
                    return Token(rule.type, match.value)
                }
            }
            return null
        }

        private fun skip(state: LexState, rules: List<CaptureRule>): Boolean {
            for (rule in rules) {
                val match = rule.regex.matchAt(state.input, state.offset)
                if (match != null) {
                    state.skip(match.value.length)
                    return true
                }
            }
            return false
        }
    }

}

object KasumiTokenTypes {
    val FUNCTION = TokenType()
    val VAR = TokenType()
    val AS = TokenType()

    val STRING = TokenType()
    val NUMBER = TokenType()
    val PLAYER = TokenType()
    val LOCATION = TokenType()

    val COLON = TokenType()
    val SEMICOLON = TokenType()
    val LBRACE = TokenType()
    val RBRACE = TokenType()
    val LBRACKET = TokenType()
    val RBRACKET = TokenType()
    val LPAREN = TokenType()
    val RPAREN = TokenType()
    val MARK = TokenType() // 无关紧要的标点

    val WORD = TokenType() // 自定义的，句子中语法性的词
}
