package serializer

import core.*

// REDUNDANT
class JSONConverter : JSONSerializer<JSONElement<*>> {
    override fun serialize(input: JSONElement<*>): String {
        return input.serialize()
    }
}