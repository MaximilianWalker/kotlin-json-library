package core

class JSONObject(override val element: MutableMap<String, JSONElement<*>> = mutableMapOf()) : JSONElement<MutableMap<String, JSONElement<*>>> {

    fun forEach(callback: (key: String, value: JSONElement<*>) -> Unit) {
        element.forEach { (key, value) -> callback(key, value) }
    }

    fun map(callback: (key: String, value: JSONElement<*>) -> JSONElement<*>): MutableMap<String, JSONElement<*>> {
        val result = mutableMapOf<String, JSONElement<*>>()
        forEach { key, value -> result[key] = callback(key, value) }
        return result
    }
}