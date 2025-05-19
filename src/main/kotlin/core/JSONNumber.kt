package core

data class JSONNumber(override val element: Number) : JSONElement<Number> {
    override fun toString(): String {
        return element.toString()
    }
}
