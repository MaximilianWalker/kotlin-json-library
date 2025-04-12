package serializer

import core.*

fun JSONElement<*>.serialize(): String {
    return when (this) {
        is JSONNumber -> this.serialize()
        is JSONString -> this.serialize()
        is JSONBoolean -> this.serialize()
        is JSONArray -> this.serialize()
        is JSONObject -> this.serialize()
        is JSONNull -> this.serialize()
    }
}

fun JSONNumber.serialize(): String {
    return element.toString()
}

fun JSONString.serialize(): String {
    return """\"$element\""""
}

fun JSONBoolean.serialize(): String {
    return element.toString().lowercase()
}

fun JSONArray.serialize(): String {
    return element.toString().lowercase()
}

fun JSONObject.serialize(): String {
    return element.toString().lowercase()
}

fun JSONNull.serialize(): String {
    return "null"
}