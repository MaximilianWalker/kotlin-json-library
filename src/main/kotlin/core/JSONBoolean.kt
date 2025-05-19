package core

data class JSONBoolean(override val element: Boolean) : JSONElement<Boolean> {
    override fun toString(): String {
        return element.toString()
    }
}
