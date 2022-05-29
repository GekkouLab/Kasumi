package gl.ky.kasumi.core

class ParseState(val tokens: TokenStream, val offset: Int)

interface ActionParser {
    fun parse(input: String): Action?
}