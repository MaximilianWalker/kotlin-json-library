package core

data class JSONString(override val element: String) : JSONElement<String> {
    override fun toString(): String {
        return element
    }
}