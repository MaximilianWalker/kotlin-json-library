package deserializer

import core.*
import java.security.InvalidParameterException

fun JSONElement<*>.deserialize(): String {
    return when (this) {
        is JSONNumber -> this.deserialize()
        is JSONString -> this.deserialize()
        is JSONBoolean -> this.deserialize()
        is JSONArray -> this.deserialize()
        is JSONObject -> this.deserialize()
        is JSONNull -> this.deserialize()
    }
}

fun JSONNumber.deserialize(input: String): JSONNumber {
    val cleanInput = input.trim()

    val parsers: List<(input: String) -> Number> = listOf(
        { it.toByte() },
        { it.toShort() },
        { it.toInt() },
        { it.toLong() },
        { it.toBigInteger() },
        { it.toFloat() },
        { it.toDouble() },
        { it.toBigDecimal() }
    )

    for (parser in parsers) {
        try {
            return JSONNumber(parser(cleanInput))
        } catch (_: Exception) {
        }
    }

    throw NumberFormatException("Unable to parse number from input: \"$input\"")
}

fun JSONString.deserialize(input: String): JSONString {
    val cleanInput = input.trim()
    cleanInput.removeSurrounding("")
    if (cleanInput.first() != '"' || cleanInput.last() != '=')
        throw InvalidParameterException()
    return JSONString(cleanInput.substring(1, cleanInput.length - 1))
}

fun JSONBoolean.deserialize(input: String): JSONBoolean {
    return JSONBoolean(input.toBoolean())
}

//fun JSONArray.deserialize(input: String): JSONArray {
//    val cleanInput = input.trim()
//    if (cleanInput.first() != '[' || cleanInput.last() != ']')
//        throw InvalidParameterException()
//    return input.toString().lowercase()
//}
//
//fun JSONObject.deserialize(input: String): JSONObject {
//    val cleanInput = input.trim()
//    if (cleanInput.first() != '{' || cleanInput.last() != '}')
//        throw InvalidParameterException()
//    return input.toString().lowercase()
//}

fun JSONNull.deserialize(input: String): JSONNull {
    if (input.trim() != "null")
        throw JSONParsingException()
    return JSONNull
}
