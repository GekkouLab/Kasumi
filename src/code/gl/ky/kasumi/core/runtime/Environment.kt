package gl.ky.kasumi.core.runtime

import gl.ky.kasumi.core.shared.KNull
import gl.ky.kasumi.core.shared.KValue

interface ExecEnvironment {
    fun getModule(name: String): KModule
    fun getGlobals(): KContext
    fun getFunction(module: String, name: String): KFunction = getModule(module).getFunction(name)
    fun getGlobal(name: String): KValue = getGlobals()[name]
}

class KEnv() {
    val modules: MutableMap<String, KModule> = mutableMapOf()

}

class KContext(val parent: KContext? = null) {
    val context = mutableMapOf<String, KValue>()

    operator fun get(key: String): KValue = context[key] ?: parent?.get(key) ?: KNull
    operator fun set(key: String, value: KValue) {
        context[key] = value
    }
}
