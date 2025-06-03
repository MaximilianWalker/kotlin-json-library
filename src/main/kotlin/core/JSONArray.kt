package core

data class JSONArray(override val element: MutableList<JSONElement<*>>) :
    JSONElement<MutableList<JSONElement<*>>>, MutableList<JSONElement<*>> by element {

    // Same VVM Signature Bug
    // constructor(elements: List<JSONElement<*>>) : this(elements.toMutableList())
    constructor(vararg elements: JSONElement<*>) : this(elements.toMutableList())
}