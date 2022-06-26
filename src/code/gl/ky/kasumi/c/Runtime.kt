package gl.ky.kasumi.c

class Environment {
    // val moduleScope = mutableMapOf<String, KValue>()
    var functionScope = mutableMapOf<String, KValue>()
    var sentenceScope = mapOf<String, KValue>()
    var capturedScope = mapOf<String, KValue>()

    operator fun get(key: String): KValue = capturedScope[key] ?: sentenceScope[key] ?: functionScope[key] ?: KNull
    // ?: moduleScope[key]
    operator fun set(key: String, value: KValue) { functionScope[key] = value }
    operator fun set(key: String, value: Any?) { functionScope[key] = KValue.wrap(value) }
}

class AstEvaluator {
    val handlerPool: MutableMap<String, (Environment) -> Unit> = mutableMapOf()
    val env: Environment = Environment()

    fun execute(s: AstNode.Segment) {
        s.sentences.forEach(this::execute)
        env.functionScope.clear()
    }

    fun execute(s: AstNode.Sentence) {
        env.sentenceScope = s.scopedVars.mapValues { it.value.value }
        s.actions.forEach(this::execute)
    }

    fun execute(s: AstNode.Action) {
        env.capturedScope = s.captured.mapValues { it.value.eval(env) }
        handlerPool[s.handler]!!.invoke(env)
    }
}
