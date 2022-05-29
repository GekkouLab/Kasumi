package gl.ky.kasumi.old

object Interpreter {
    val scriptPool: MutableMap<String, ScriptGroup> = mutableMapOf()
    val actionPool: MutableMap<String, (Environment) -> Unit> = mutableMapOf()
}

class Environment(val bindings: MutableMap<String, Any>) {
    operator fun get(name: String): Any? = bindings[name]
    operator fun set(name: String, value: Any) {
        bindings[name] = value
    }
    fun <T> getAs(name: String): T? = bindings[name] as? T
}

