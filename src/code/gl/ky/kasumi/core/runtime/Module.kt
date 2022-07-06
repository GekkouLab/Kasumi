package gl.ky.kasumi.core.runtime

import gl.ky.kasumi.core.compile.KNode
import gl.ky.kasumi.core.shared.KValue

interface KModule {
    fun getFunction(name: String): KFunction
}

class KScriptModule : KModule {
    override fun getFunction(name: String): KFunction {
        TODO()
    }
}


typealias KFunction = (KEnv, KContext, List<KValue>) -> KValue

class KScriptFunction(val body: KNode.Module) : KFunction{
    override fun invoke(env: KEnv, context: KContext, args: List<KValue>): KValue {
        TODO()
    }
}

typealias JvmFunction = (KEnv, KContext, List<KValue>) -> KValue
