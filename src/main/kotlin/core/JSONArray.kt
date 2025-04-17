package core

class JSONArray(override val element: MutableList<JSONElement<*>>) :
    JSONElement<MutableList<JSONElement<*>>>, MutableList<JSONElement<*>> by element {

    constructor(vararg elements: JSONElement<*>) : this(elements.toMutableList())

    fun <T> map(callback: (value: JSONElement<*>) -> T): MutableList<T> {
        val result = mutableListOf<T>()
        forEach { value -> result.add(callback(value)) }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JSONArray) return false
        return (0..element.size - 1).map { this[it].equals(other[it]) }.reduce { a, b -> a && b }
    }

    override fun hashCode(): Int {
        return element.hashCode()
    }
}