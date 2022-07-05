package gl.ky.kasumi.core.compile

typealias TokenStream = List<Token>

class Token(val type: TokenType, val content: String)

class TokenType {
    companion object {
        @JvmStatic
        val SKIP = TokenType()
    }
}

abstract class LexRule private constructor(val type: TokenType, val transformer: ((String) -> String)?) {
    companion object {
        @JvmStatic
        fun fullMatch(type: TokenType, pattern: String, transformer: ((String) -> String)? = null): LexRule =
            if (pattern.length == 1) OneChar(type, transformer, pattern[0]) else FullMatch(type, transformer, pattern)

        @JvmStatic
        fun regex(type: TokenType, pattern: String, transformer: ((String) -> String)? = null): LexRule =
            Regex(type, transformer, pattern)

        @JvmStatic
        fun any(type: TokenType, pattern: List<String>, transformer: ((String) -> String)? = null): LexRule =
            AnyOf(type, transformer, pattern)
    }

    fun match(input: String, start: Int): Token? {
        var s = capture(input, start)
        s ?: return null
        if (transformer != null) s = transformer.invoke(s)
        return Token(type, s)
    }

    abstract fun capture(input: String, start: Int): String?

    private class AnyOf(type: TokenType, transformer: ((String) -> String)?, val words: List<String>) :
        LexRule(type, transformer) {
        override fun capture(input: String, start: Int): String? {
            for (s in words) if (input.startsWith(s, start)) return s
            return null
        }
    }

    private class OneChar(type: TokenType, transformer: ((String) -> String)?, val c: Char) :
        LexRule(type, transformer) {
        override fun capture(input: String, start: Int): String? = if (input[start] == c) c.toString() else null
    }

    private class FullMatch(type: TokenType, transformer: ((String) -> String)?, val s: String) :
        LexRule(type, transformer) {
        override fun capture(input: String, start: Int): String? = if (input.startsWith(s, start)) s else null
    }

    private class Regex(type: TokenType, transformer: ((String) -> String)?, re: String) : LexRule(type, transformer) {
        val regex = re.toRegex()
        override fun capture(input: String, start: Int): String? {
            return regex.matchAt(input, start)?.value
        }
    }
}

class Lexer(val rules: List<LexRule>) {
    fun lex(input: String): TokenStream {
        var offset = 0
        val tokens = mutableListOf<Token>()
        while (offset < input.length) {
            var result: Token? = null
            for (rule in rules) {
                result = rule.match(input, offset) ?: continue
            }
            if (result == null) throw RuntimeException("No rule matched")
            if (result.type != TokenType.SKIP) tokens.add(result)
            offset += result.content.length
        }
        return tokens
    }
}

class NewLexer(val matches: List<LexRule>, val skip: List<LexRule>, val input: String, var offset: Int = 0) {
    private val mapped = mutableMapOf<TokenType, MutableList<LexRule>>().also {
        for (rule in matches) {
            it.getOrPut(rule.type) { mutableListOf() }.add(rule)
        }
    }

    private fun skip() {
        a@ while (offset < input.length) {
            for (rule in skip) {
                val result = rule.match(input, offset) ?: continue@a
                offset += result.content.length
            }
        }
    }

    fun next(type: TokenType): Token? {
        return next(mapped[type] ?: return null)
    }

    fun next(rules: List<LexRule>): Token? {
        skip()
        for (rule in rules) {
            return rule.match(input, offset) ?: continue
        }
        return null
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
