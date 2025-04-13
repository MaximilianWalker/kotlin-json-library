package serializer

import core.*
import deserializer.JSONDeserializer

class JSONConverter : JSONSerializer(), JSONDeserializer<JSONElement<*>> {
    override fun serializeNumber(element: JSONNumber): String {
        return element.toString()
    }

    override fun serializeString(element: JSONString): String {
        return "\"$element\""
    }

    override fun serializeBoolean(element: JSONBoolean): String {
        return element.toString().lowercase()
    }

    override fun serializeArray(element: JSONArray, options: JSONSerializationOptions): String {
        if (options.prettyPrint) {
            val inner = element.joinToString(",\n") {
                options.indent + serialize(it, options).prependIndent(options.indent)
            }
            return "[\n$inner\n]"
        }
        return "[${element.joinToString(",") { serialize(it, options) }}]"
    }

    override fun serializeObject(element: JSONObject, options: JSONSerializationOptions): String {
        val keys = if (options.sortKeys) element.keys.sorted() else element.keys

        if (options.prettyPrint) {
            val inner = keys.joinToString(",\n") { key ->
                val safeKey = "\"" + key.replace("\"", "\\\"") + "\""
                val value = element[key]?.let { serialize(it, options) }?.prependIndent(options.indent) ?: "null"
                "${options.indent}$safeKey: $value"
            }
            return "{\n$inner\n}"
        }

        val inner = keys.joinToString(",") { key ->
            val safeKey = "\"" + key.replace("\"", "\\\"") + "\""
            val value = element[key]?.let { serialize(it, options) } ?: "null"
            "$safeKey:$value"
        }

        return "{$inner}"
    }

    override fun serializeNull(): String {
        return "null"
    }
}