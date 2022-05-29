package gl.ky.kasumi.core

interface AstNode {
    class Segment(val sentences: List<Sentence>) : AstNode
    class Sentence(val actions: List<Action>) : AstNode
    class Action(val exprs: Map<String, Value>, val handler: String) : AstNode
    class Value(val value: Any) : AstNode
}