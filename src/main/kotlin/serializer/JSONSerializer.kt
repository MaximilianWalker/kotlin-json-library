package serializer

import core.*

abstract class JSONSerializer : TextSerializer<JSONElement<*>, JSONSerializationOptions> {
    override fun serialize(input: JSONElement<*>): String {
        return serialize(input, JSONSerializationOptions())
    }

    override fun serialize(input: JSONElement<*>, options: JSONSerializationOptions): String {
        return when (input) {
            is JSONNumber -> serializeNumber(input)
            is JSONString -> serializeString(input)
            is JSONBoolean -> serializeBoolean(input)
            is JSONArray -> serializeArray(input, options)
            is JSONObject -> serializeObject(input, options)
            is JSONNull -> serializeNull()
        }
    }

    protected abstract fun serializeNumber(element: JSONNumber): String
    protected abstract fun serializeString(element: JSONString): String
    protected abstract fun serializeBoolean(element: JSONBoolean): String
    protected abstract fun serializeArray(element: JSONArray, options: JSONSerializationOptions): String
    protected abstract fun serializeObject(element: JSONObject, options: JSONSerializationOptions): String
    protected abstract fun serializeNull(): String
}