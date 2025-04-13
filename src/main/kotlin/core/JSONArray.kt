package core

class JSONArray(override val element: MutableList<JSONElement<*>>) : JSONElement<MutableList<JSONElement<*>>>, Iterable<JSONElement<*>> {

    fun get(index: Int): JSONElement<*> {
        return element[index]
    }

    fun first(): JSONElement<*> {
        return element.first()
    }

    fun last(): JSONElement<*> {
        return element.last()
    }

    fun isEmpty(): Boolean {
        return element.isEmpty()
    }

    fun add(value: JSONElement<*>) {
        element.add(value)
    }

    fun add(index: Int, value: JSONElement<*>) {
        element.add(index, value)
    }

    fun remove(value: JSONElement<*>): Boolean {
        return element.remove(value)
    }

    fun removeAt(index: Int): JSONElement<*> {
        return element.removeAt(index)
    }

    override fun iterator(): Iterator<JSONElement<*>> {
        return element.iterator()
    }

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