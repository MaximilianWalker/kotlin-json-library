package core

class JSONBoolean(override val element: Boolean) : JSONElement<Boolean> {
    override fun toString(): String {
        return element.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONBoolean) return false
        return element == other.element
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}
