package core

class JSONBoolean(override val element: Boolean) : JSONElement<Boolean> {
    override fun forEach(callback: (element: JSONElement<*>, index: Int) -> Unit) {
        callback(this, index)
    }
}
