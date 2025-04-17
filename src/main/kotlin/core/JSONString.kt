package core

class JSONString(override val element: String) : JSONElement<String> {
    override fun toString(): String {
        return element
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONString) return false
        return element == other.element
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}