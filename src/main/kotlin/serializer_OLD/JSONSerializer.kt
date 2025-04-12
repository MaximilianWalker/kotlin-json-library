package serializer_OLD

import core.JSONElement

class JSONSerializer : TextSerializer<JSONElement<*>> {
    override fun serialize(input: JSONElement<*>): String {
        return input.ser
    }
}