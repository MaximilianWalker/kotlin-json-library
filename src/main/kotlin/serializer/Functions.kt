package serializer

import core.*

fun JSONElement<*>.serialize(serializer: JSONSerializer = JSONConverter()): String {
    return serializer.serialize(this)
}