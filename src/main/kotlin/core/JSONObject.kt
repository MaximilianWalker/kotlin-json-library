package core

class JSONObject(override val element: MutableMap<String, JSONElement<*>>) :
    JSONElement<MutableMap<String, JSONElement<*>>>, MutableMap<String, JSONElement<*>> by element {

    constructor(vararg pairs: Pair<String, JSONElement<*>>): this(pairs.toMap().toMutableMap())

//    constructor(map: Map<String, JSONElement<*>>) : this(map.toMutableMap())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONObject) return false
        return element == other.element
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}