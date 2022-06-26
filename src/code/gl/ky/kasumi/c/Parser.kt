package gl.ky.kasumi.c

class ParseState(val tokens: TokenStream, val offset: Int)

object ParseUtil {
    fun ParseState.match(s: String) = this.tokens[offset].also { if(it.value != s) throw RuntimeException() }
    fun ParseState.match(t: TokenType) =
}

interface ActionParser {
    fun parse(input: ParseState): AstNode.Action?
}

/**
 * Parses a runtime transformer.
 */
interface TransformerParser {
    fun parse(input: ParseState): ((KValue, Environment) -> KValue>)?
}

interface ConstModifier {
    fun parse(input: ParseState, v: KValue): KValue?
}

class Parser(ap: List<ActionParser>) {

    fun parse(input: TokenStream) : AstNode.Module = parseModule(ParseState(input, 0))

    fun parseModule(state: ParseState): AstNode.Module {
        val segments = mutableMapOf<String, AstNode.Segment>()
        while (state.offset < state.tokens.size) {
            val s = parseSegment(state)
            segments += s.name to s
        }
        return AstNode.Module(segments)
    }

    fun parseSegment(state: ParseState): AstNode.Segment {
        val sentences = mutableListOf<AstNode.Sentence>()
        state.match(KasumiTokenTypes.SEGMENT)
        nameT = state.match(KasumiTokenTypes.WORD)

        while (state.offset < state.tokens.size) {
            sentences += parseSentence(state)
        }
        return AstNode.Segment(nameT, sentences)
    }

}
