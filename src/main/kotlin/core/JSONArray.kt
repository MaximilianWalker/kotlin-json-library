package core

class JSONArray(override val element: MutableList<JSONElement<*>>) : JSONElement<MutableList<JSONElement<*>>> {

    fun forEach(callback: (value: JSONElement<*>) -> Unit) {
        element.forEach { value -> callback(value) }
    }

    fun forEachIndexed(callback: (index: Int, value: JSONElement<*>) -> Unit) {
        element.forEachIndexed { index, value -> callback(index, value) }
    }

    fun map(callback: (value: JSONElement<*>) -> JSONElement<*>): MutableList<JSONElement<*>> {
        val result = mutableListOf<JSONElement<*>>()
        forEach { value -> result.add(callback(value)) }
        return result
    }
}