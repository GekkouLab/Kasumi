package gl.ky.kasumi.core.shared

interface KValue

interface KPrimitive : KValue, Cloneable {

    override fun clone(): KValue
}

class KNumber(val value: Int) : KPrimitive {
    override fun clone(): KValue = KNumber(value)
}

class KString(val value: String) : KPrimitive {
    override fun clone(): KValue = KString(value)
}

class KBoolean private constructor(val value: Boolean) : KPrimitive {
    companion object {

        val TRUE = KBoolean(true)
        val FALSE = KBoolean(false)
    }

    override fun clone(): KValue = if (value) TRUE else FALSE
}

object KNull : KPrimitive {
    override fun clone(): KValue = KNull
}
