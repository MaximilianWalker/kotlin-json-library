package core

class JSONObject : JSONElement<MutableMap<String, JSONElement<*>>> {
    override val element: MutableMap<String, JSONElement<*>>

    constructor(vararg pairs: Pair<String, JSONElement<*>>) {
        element = pairs.toMap().toMutableMap()
    }

    constructor(map: Map<String, JSONElement<*>>) {
        element = map.toMutableMap()
    }

    val keys: Set<String> get() = element.keys
    val values: Collection<JSONElement<*>> get() = element.values

    operator fun get(key: String): JSONElement<*>? {
        return element[key]
    }

    operator fun set(key: String, value: JSONElement<*>) {
        element[key] = value
    }

    fun forEach(callback: (key: String, value: JSONElement<*>) -> Unit) {
        element.forEach { (key, value) -> callback(key, value) }
    }

    fun map(callback: (key: String, value: JSONElement<*>) -> JSONElement<*>): JSONObject {
        val result = mutableMapOf<String, JSONElement<*>>()
        forEach { key, value -> result[key] = callback(key, value) }
        return JSONObject(result)
    }

    fun filter(predicate: (key: String, value: JSONElement<*>) -> Boolean): JSONObject {
        val result = element.filter { predicate(it.key, it.value) }
        return JSONObject(result.toMap())
    }
}