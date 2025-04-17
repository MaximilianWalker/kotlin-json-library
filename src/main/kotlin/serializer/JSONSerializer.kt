package serializer

import core.*

interface JSONSerializer : TextSerializer<JSONElement<*>, JSONSerializationOptions> {
    override fun serialize(input: JSONElement<*>): String {
        return serializeWithOptions(input, JSONSerializationOptions())
    }

    override fun serializeWithOptions(input: JSONElement<*>, options: JSONSerializationOptions): String {
        return when (input) {
            is JSONNumber -> serializeNumber(input)
            is JSONString -> serializeString(input)
            is JSONBoolean -> serializeBoolean(input)
            is JSONArray -> serializeArray(input, options)
            is JSONObject -> serializeObject(input, options)
            is JSONNull -> serializeNull()
        }
    }

    fun serializeNumber(element: JSONNumber): String
    fun serializeString(element: JSONString): String
    fun serializeBoolean(element: JSONBoolean): String
    fun serializeArray(element: JSONArray, options: JSONSerializationOptions): String
    fun serializeObject(element: JSONObject, options: JSONSerializationOptions): String
    fun serializeNull(): String
}