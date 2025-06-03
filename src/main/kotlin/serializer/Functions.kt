package serializer

import converter.JSONConverter
import core.*

fun JSONElement<*>.serialize(serializer: JSONSerializer = JSONConverter()): String {
    return serializer.serialize(this)
}