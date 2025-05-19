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

    fun serializeNumber(jsonElement: JSONNumber): String
    fun serializeString(jsonElement: JSONString): String
    fun serializeBoolean(jsonElement: JSONBoolean): String
    fun serializeArray(jsonElement: JSONArray, options: JSONSerializationOptions): String
    fun serializeObject(jsonElement: JSONObject, options: JSONSerializationOptions): String
    fun serializeNull(): String
}