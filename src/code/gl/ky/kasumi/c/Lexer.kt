package gl.ky.kasumi.c

import java.util.regex.Matcher
import java.util.regex.Pattern

typealias TokenStream = List<Token>

data class Token(val type: TokenType, val value: String)

class TokenType {
    companion object {
        @JvmStatic val SKIP = TokenType()
    }
}

abstract class LexRule(val type: TokenType, val transformer: ((String) -> String)?) {
    fun match(input: String, start: Int): Token? {
        var s = capture(input, start)
        s ?: return null
        if(transformer != null) s = transformer.invoke(s)
        return Token(type, s)
    }

    abstract fun capture(input: String, start: Int): String?

    companion object {
        @JvmStatic fun fullMatch(type: TokenType, pattern: String, transformer: ((String) -> String)? = null)
            = if(pattern.length == 1) OneChar(type, transformer, pattern[0]) else FullMatch(type, transformer, pattern)

        @JvmStatic fun regex(type: TokenType, pattern: String, transformer: ((String) -> String)? = null)
            = Regex(type, transformer, Pattern.compile(pattern))

        @JvmStatic fun any(type: TokenType, pattern: List<String>, transformer: ((String) -> String)? = null)
            = AnyOf(type, transformer, pattern)
    }

    class AnyOf(type: TokenType, transformer: ((String) -> String)?, val words: List<String>)
        : LexRule(type, transformer) {
        override fun capture(input: String, start: Int): String? {
            for(s in words) if(input.startsWith(s, start)) return s
            return null
        }
    }

    class OneChar(type: TokenType, transformer: ((String) -> String)?, val c: Char)
        : LexRule(type, transformer) {
        override fun capture(input: String, start: Int): String? = if(input[start] == c) c.toString() else null
    }

    class FullMatch(type: TokenType, transformer: ((String) -> String)?, val s: String)
        : LexRule(type, transformer) {
        override fun capture(input: String, start: Int): String? = if(input.startsWith(s, start)) s else null
    }

    class Regex(type: TokenType, transformer: ((String) -> String)?, pattern: Pattern) : LexRule(type, transformer) {
        val matcher: Matcher = pattern.matcher("")
        override fun capture(input: String, start: Int): String? {
            matcher.reset(input)
            return if (matcher.find(start) && matcher.start() == 0) matcher.group() else null
        }
    }
}

class Lexer(val rules: List<LexRule>) {
    fun lex(input: String): TokenStream {
        var offset = 0
        val tokens = mutableListOf<Token>()
        while (offset < input.length) {
            var result: Token? = null
            for(rule in rules) {
                result = rule.match(input, offset) ?: continue
            }
            if (result == null) throw RuntimeException("No rule matched")
            if(result.type != TokenType.SKIP) tokens.add(result)
            offset += result.value.length
        }
        return tokens
    }
}

object KasumiTokenTypes {
    val SEGMENT = TokenType()

    val STRING = TokenType()
    val NUMBER = TokenType()
    val PLAYER = TokenType()
    val LOCATION = TokenType()

    val LBRACE = TokenType()
    val RBRACE = TokenType()
    val LBRACKET = TokenType()
    val RBRACKET = TokenType()
    val LPAREN = TokenType()
    val RPAREN = TokenType()
    val MARK = TokenType() // 无关紧要的标点

    val WORD = TokenType() // 自定义的，句子中语法性的词
}