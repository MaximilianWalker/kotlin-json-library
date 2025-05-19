package converter

import core.*
import deserializer.JSONDeserializer
import serializer.JSONSerializationOptions
import serializer.JSONSerializer

class JSONConverter : JSONSerializer, JSONDeserializer {

    fun splitByComma(input: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var bracketCount = 0

        for (char in input) {
            when (char) {
                '"' -> inQuotes = !inQuotes
                '{' -> bracketCount++
                '}' -> bracketCount--
                '[' -> bracketCount++
                ']' -> bracketCount--
                ',' -> if (!inQuotes && bracketCount == 0) {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                    continue
                }
            }
            current.append(char)
        }

        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result
    }

    //region Deserialization

    override fun deserializeNumber(input: String): JSONNumber {
        val parsers: List<(input: String) -> Number> = listOf(
            { it.toInt() },
            { it.toByte() },
            { it.toShort() },
            { it.toLong() },
            { it.toBigInteger() },
            { it.toFloat() },
            { it.toDouble() },
            { it.toBigDecimal() }
        )

        for (parser in parsers) {
            try {
                return JSONNumber(parser(input))
            } catch (_: Exception) {
            }
        }

        throw NumberFormatException("Unable to parse number from input: \"$input\"")
    }

    override fun deserializeString(input: String): JSONString {
        val cleanInput = input.removeSurrounding("\"")
        return JSONString(cleanInput)
    }

    override fun deserializeBoolean(input: String): JSONBoolean {
        return JSONBoolean(input.toBoolean())
    }

    override fun deserializeArray(input: String): JSONArray {
        val cleanInput = input.removeSurrounding("[", "]")
        val elements = splitByComma(cleanInput).map { deserialize(it) }
        return JSONArray(elements.toMutableList())
    }

    override fun deserializeObject(input: String): JSONObject {
        val cleanInput = input.removeSurrounding("{", "}")
        val elements = splitByComma(cleanInput).associate { element ->
            val (key, value) = element.split(":", limit = 2).map { it.trim() }
            val cleanKey = key.removeSurrounding("\"")
            cleanKey to deserialize(value)
        }
        return JSONObject(elements.toMutableMap())
    }

    //endregion

    //region Serialization

    override fun serializeNumber(jsonElement: JSONNumber): String {
        return jsonElement.element.toString()
    }

    override fun serializeString(jsonElement: JSONString): String {
        val output = jsonElement.element
        return "\"$output\""
    }

    override fun serializeBoolean(jsonElement: JSONBoolean): String {
        return jsonElement.element.toString().lowercase()
    }

    override fun serializeArray(jsonElement: JSONArray, options: JSONSerializationOptions): String {
        if (options.prettyPrint) {
            val inner = jsonElement.element.joinToString(",\n") {
                options.indent + serializeWithOptions(it, options).prependIndent(options.indent)
            }
            return "[\n$inner\n]"
        }
        return "[${jsonElement.element.joinToString(",") { serializeWithOptions(it, options) }}]"
    }

    override fun serializeObject(jsonElement: JSONObject, options: JSONSerializationOptions): String {
        val keys = if (options.sortKeys) jsonElement.element.keys.sorted() else jsonElement.element.keys

        if (options.prettyPrint) {
            val inner = keys.joinToString(",\n") { key ->
                val safeKey = "\"" + key.replace("\"", "\\\"") + "\""
                val value = jsonElement.element[key]?.let { serializeWithOptions(it, options) }?.prependIndent(options.indent) ?: "null"
                "${options.indent}$safeKey: $value"
            }
            return "{\n$inner\n}"
        }

        val inner = keys.joinToString(",") { key ->
            val safeKey = "\"" + key.replace("\"", "\\\"") + "\""
            val value = jsonElement.element[key]?.let { serializeWithOptions(it, options) } ?: "null"
            "$safeKey:$value"
        }

        return "{$inner}"
    }

    override fun serializeNull(): String {
        return "null"
    }

    //endregion
}