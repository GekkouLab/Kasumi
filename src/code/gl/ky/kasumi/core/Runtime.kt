package gl.ky.kasumi.core

class Environment(val parent: Environment? = null) {
    val store = mutableMapOf<String, Any>()

    constructor(initial: Map<String, Any>, parent: Environment? = null): this() {
        store += initial
    }

    operator fun get(key: String): Any? = store[key] ?: parent?.get(key)
    operator fun set(key: String, value: Any) { store[key] = value }
}

class AstEvaluator {
    val handlerPool: MutableMap<String, (Environment) -> Unit> = mutableMapOf()
    val globalEnv: Environment = Environment()

    fun execute(s: AstNode.Segment, parent: Environment = globalEnv) {
        val env = Environment(parent)
        s.sentences.forEach {
            execute(it, env)
        }
    }

    fun execute(s: AstNode.Sentence, parent: Environment) {
        val env = Environment(parent)
        s.actions.forEach {
            execute(it, env)
        }
    }

    fun execute(s: AstNode.Action, env: Environment) {
        s.captured.forEach { t, u -> env[t] = u  }
        handlerPool[s.handler]!!.invoke(env)
    }
}