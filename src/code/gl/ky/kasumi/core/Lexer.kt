package gl.ky.kasumi.core

import java.util.regex.Pattern

typealias TokenStream = List<Token>

class Token(val type: TokenType, val value: String)

class TokenType {
    companion object {
        val NUMBER = TokenType()
        val STRING = TokenType()
        val PLAYER = TokenType()
        val LOCATION = TokenType()
        val LBRACE = TokenType() // {
        val RBRACE = TokenType() // }
        val LBRACKET = TokenType() // [
        val RBRACKET = TokenType() // ]
        val MARK = TokenType() // 无关紧要的标点
    }
}

interface LexRule {
    enum class Mode { FULL_MATCH, REGEX, ANY_OF }

    fun match(input: String, start: Int): Token?

    companion object {
        val NUMBER = LexRule.of(TokenType.NUMBER, "\\d+", Mode.REGEX)
        val STRING = LexRule.of(TokenType.STRING, "\"[^\"]*\"", Mode.REGEX)
        val PLAYER = LexRule.of(TokenType.PLAYER, "@[a-zA-Z0-9_]+", Mode.REGEX)
        val LOCATION = LexRule.of(TokenType.LOCATION, "\\[\\d+,\\d+,\\d+\\]", Mode.REGEX)

        @JvmStatic
        fun of(type: TokenType, pattern: Char, mode: Mode = Mode.FULL_MATCH) = OneChar(type, pattern)

        @JvmStatic
        fun of(type: TokenType, pattern: List<String>, mode: Mode = Mode.ANY_OF) = AnyOf(type, pattern)

        @JvmStatic
        fun of(type: TokenType, pattern: String, mode: Mode = Mode.FULL_MATCH): LexRule {
            return when(mode) {
                Mode.FULL_MATCH -> if (pattern.length != 1) FullMatch(type, pattern)
                    else OneChar(type, pattern[0])
                Mode.REGEX -> Regex(type, Pattern.compile(pattern))
                Mode.ANY_OF -> AnyOf(type, pattern.split("|"))
            }
        }
    }

    class AnyOf(val type: TokenType, val patterns: List<String>) : LexRule {
        override fun match(input: String, start: Int): Token? {
            for(s in patterns) if(input.startsWith(s, start)) return Token(type, s)
            return null
        }
    }

    class OneChar(val type: TokenType, val c: Char) : LexRule {
        override fun match(input: String, start: Int) =
            if(input[start] == c) Token(type, c.toString()) else null
    }

    class FullMatch(val type: TokenType, val pattern: String) : LexRule {
        override fun match(input: String, start: Int) =
            if(input.startsWith(pattern, start)) Token(type, pattern) else null
    }

    class Regex(val type: TokenType, val pattern: Pattern) : LexRule {
        override fun match(input: String, start: Int): Token? {
            val matcher = pattern.matcher(input)
            return if (matcher.find(start) && matcher.start() == 0)
                Token(type, matcher.group()) else null
        }
    }
}

class Lexer(val rules: List<LexRule>) {
    fun analyze(input: String): List<Token> {
        var offset = 0
        val tokens = mutableListOf<Token>()
        while (offset < input.length) {
            var result: Token? = null
            for(rule in rules) {
                result = rule.match(input, offset) ?: continue
            }
            if (result == null) throw RuntimeException("No rule matched")
            tokens.add(result)
            offset += result.value.length
        }
        return tokens
    }
}