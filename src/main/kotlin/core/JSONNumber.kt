package core

class JSONNumber(override val element: Number) : JSONElement<Number> {
    override fun toString(): String {
        return element.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONNumber) return false
        return element == other.element
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}
