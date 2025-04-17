package core

class JSONArray(override val element: MutableList<JSONElement<*>>) :
    JSONElement<MutableList<JSONElement<*>>>, MutableList<JSONElement<*>> by element {

        // Same VVM Signature Bug
//    constructor(elements: List<JSONElement<*>>) : this(elements.toMutableList())
    constructor(vararg elements: JSONElement<*>) : this(elements.toMutableList())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONArray || size != other.size) return false
        return (0..<size).all { this[it] == other[it] }
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}