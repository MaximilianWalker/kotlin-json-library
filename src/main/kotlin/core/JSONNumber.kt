package core

class JSONNumber(override val element: Number) : JSONElement<Number> {
    override fun toString(): String {
        return element.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONNumber) return false
        return when (element) {
            is Int -> element == other.element.toInt()
            is Double -> element == other.element.toDouble()
            is Float -> element == other.element.toFloat()
            is Long -> element == other.element.toLong()
            is Short -> element == other.element.toShort()
            else -> false
        }
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}
