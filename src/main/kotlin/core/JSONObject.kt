package core

data class JSONObject(override val element: MutableMap<String, JSONElement<*>>) :
    JSONElement<MutableMap<String, JSONElement<*>>>, MutableMap<String, JSONElement<*>> by element {

    // Same VVM Signature Bug
    // constructor(map: Map<String, JSONElement<*>>) : this(map.toMutableMap())
    constructor(vararg pairs: Pair<String, JSONElement<*>>) : this(pairs.toMap().toMutableMap())
}