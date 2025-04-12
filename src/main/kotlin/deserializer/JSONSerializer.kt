package deserializer

import core.*

interface JSONSerializer<T> : TextSerializer<T>

class JSONConverter : JSONSerializer<JSONElement<*>> {
    override fun serialize(input: JSONElement<*>): String {
        return when (input) {
            is JSONNumber -> input.serialize()
            is JSONString -> input.serialize()
            is JSONBoolean -> input.serialize()
            is JSONArray -> input.serialize()
            is JSONObject -> input.serialize()
            is JSONNull -> input.serialize()
            else -> throw IllegalArgumentException("Unsupported type: ${input::class}")
        }
    }

}

fun JSONNumber.serialize(input: Number): String {
    return input.toString()
}

fun JSONString.serialize(input: String): String {
    return """\"$input\""""
}

fun JSONBoolean.serialize(input: Boolean): String {
    return input.toString().lowercase()
}

fun JSONArray.serialize(input: Array<*>): String {
    return input.toString().lowercase()
}

fun JSONArray.serialize(input: List<*>): String {
    return input.toString().lowercase()
}

fun JSONObject.serialize(input: Boolean): String {
    return input.toString().lowercase()
}