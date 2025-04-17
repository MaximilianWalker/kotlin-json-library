package core

class JSONArray : JSONElement<MutableList<JSONElement<*>>>, Iterable<JSONElement<*>> {
    override val element: MutableList<JSONElement<*>>

    constructor(vararg elements: JSONElement<*>) {
        element = elements.toMutableList()
    }

    constructor(elements: List<JSONElement<*>>) {
        element = elements.toMutableList()
    }

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

    fun <T> map(callback: (value: JSONElement<*>) -> T): MutableList<T> {
        val result = mutableListOf<T>()
        forEach { value -> result.add(callback(value)) }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONArray) return false
        return (0..element.size - 1).map { element[it].equals(other[it] as JSONArray) }.reduce { a, b -> a && b }
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}