package gl.ky.kasumi.core

interface AstNode {
    class Module(val segments: Map<String, Segment>) : AstNode
    class Segment(val sentences: List<Sentence>) : AstNode
    class Sentence(val actions: List<Action>, val scopedVars: Map<String, Expression>) : AstNode
    class Action(val handler: String, val captured: Map<String, Expression>) : AstNode
    class Expression(val value: KValue, val transformers: List<(KValue, Environment) -> KValue>) : AstNode {
        fun eval(env: Environment): KValue {
            var acc = value
            for(transformer in transformers) acc = transformer(value, env)
            return acc
        }
    }
}
