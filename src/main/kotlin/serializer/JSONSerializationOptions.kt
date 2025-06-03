package serializer

data class JSONSerializationOptions(
    val prettyPrint: Boolean = false,
    val indent: String = "  ",
    val sortKeys: Boolean = false
)