package gl.ky.kasumi.core
/*
class ParseState(val tokens: TokenStream, val offset: Int)

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
            segments += parseSegment(state)
        }
        return AstNode.Module(segments)
    }

    fun parseSegment(state: ParseState): Pair<String, AstNode.Segment> {
        val sentences = mutableListOf<AstNode.Sentence>()
        state.match(KasumiTokenTypes.SEGMENT)
        nameT = state.match(KasumiTokenTypes.WORD)

        while (state.offset < state.tokens.size) {
            sentences += parseSentence(state)
        }
        return Pair(nameT.value, AstNode.Segment(sentences))
    }

}
*/