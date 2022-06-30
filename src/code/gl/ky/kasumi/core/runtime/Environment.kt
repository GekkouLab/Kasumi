package gl.ky.kasumi.core.runtime

import gl.ky.kasumi.core.shared.KNull
import gl.ky.kasumi.core.shared.KValue

class KContext(val parent: KContext? = null) {
    val context = mutableMapOf<String, KValue>()

    fun get(key: String): KValue = context[key] ?: parent?.get(key) ?: KNull
    fun set(key: String, value: KValue) { context[key] = value }
}