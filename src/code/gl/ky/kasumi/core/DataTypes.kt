package gl.ky.kasumi.core

interface KValue {
    interface Converter<T> {
        /**
         * Convert the value to the specified type.
         * @param value the value to convert
         * @return the converted value
         * @throws IllegalArgumentException if the value cannot be converted
         */
        fun convert(value: KValue): T
    }

    companion object {
        /**
         * Get the value as some object in the specified type.
         * @param converter the converter to use to convert the value
         * @return the converted value
         * @throws IllegalArgumentException if the value cannot be converted
         */
        inline fun <reified T> KValue.getAs(converter: Converter<T>): T = converter.convert(this)

        fun wrap(value: Int) = KInteger(value.toLong())
        fun wrap(value: Long) = KInteger(value)
        fun wrap(value: Float) = KDouble(value.toDouble())
        fun wrap(value: Double) = KDouble(value)
        fun wrap(value: Boolean) = if(value) KBoolean.TRUE else KBoolean.FALSE
        fun wrap(value: String) = KString(value)

        fun wrap(value: Any?): KValue = if(value == null) KNull else when (value) {
            is KValue -> value
            is Boolean -> if(value) KBoolean.TRUE else KBoolean.FALSE
            is Byte -> KInteger(value.toLong())
            is Short -> KInteger(value.toLong())
            is Int -> KInteger(value.toLong())
            is Long -> KInteger(value)
            is Float -> KDouble(value.toDouble())
            is Double -> KDouble(value)
            is String -> KString(value)
            else -> KJavaObjectBox(value)
        }
    }

    fun getJavaObject(): Any?
    /**
     * clone the value.
     * if value is immutable, allow to return itself.
     * @return cloned value
     */
    fun clone(): KValue

    /**
     * Get the value as a number.
     * @return the value as a number
     * @throws IllegalArgumentException if the value cannot be converted
     */
    fun getAsNumber(): Number = throw IllegalArgumentException("not able to convert to a Number")
    fun getAsString(): String  = throw IllegalArgumentException("not able to convert to a String")
    fun getAsBoolean(): Boolean  = throw IllegalArgumentException("not able to convert to a Boolean")

    /**
     * Get the value if it is a number, or throw an exception.
     * @return a number
     * @throws IllegalArgumentException if the value is not a number
     */
    fun getNumber(): Number = throw IllegalArgumentException("Not a Number")
    fun getString(): String = throw IllegalArgumentException("Not a String")
    fun getBoolean(): Boolean = throw IllegalArgumentException("Not a Boolean")
}

class KJavaObjectBox(val value: Any?) : KValue {
    override fun getJavaObject(): Any? = value
    override fun clone(): KValue = this
}

object KNull : KValue {
    override fun getJavaObject(): Any? = null
    override fun clone(): KValue = this
}

class KString(val value: String) : KValue {
    override fun getJavaObject() = value
    override fun clone(): KValue = this
    override fun getAsNumber(): Number = value.toDouble()
    override fun getAsString(): String = value
    override fun getAsBoolean(): Boolean = value.toBoolean()
    override fun getString(): String = value
}

class KBoolean private constructor(val value: Boolean) : KValue {
    companion object {
        val TRUE = KBoolean(true)
        val FALSE = KBoolean(false)
    }

    override fun getJavaObject() = value
    override fun clone(): KValue = this
    override fun getAsString(): String = value.toString()
    override fun getAsBoolean(): Boolean = value
    override fun getBoolean(): Boolean = value
}

abstract class KNumber : KValue

class KInteger(val value: Long) : KNumber() {
    override fun getJavaObject() = value
    override fun clone(): KValue = this
    override fun getAsNumber(): Number = value
    override fun getAsString(): String = value.toString()
    override fun getNumber(): Number = value
}

class KDouble(val value: Double) : KNumber() {
    override fun getJavaObject() = value
    override fun clone(): KValue = this
    override fun getAsNumber(): Number = value
    override fun getAsString(): String = value.toString()
    override fun getNumber(): Number = value
}
