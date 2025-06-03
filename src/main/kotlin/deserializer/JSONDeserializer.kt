package deserializer

import core.*

interface JSONDeserializer : TextDeserializer<JSONElement<*>> {
    override fun deserialize(input: String): JSONElement<*> {
        val cleanInput = input.trim()
        return when {
            cleanInput.first().isDigit() ->
                this.deserializeNumber(cleanInput)

            cleanInput.first() == '"' && cleanInput.last() == '"' ->
                this.deserializeString(cleanInput)

            cleanInput == "true" || cleanInput == "false" ->
                this.deserializeBoolean(cleanInput)

            cleanInput.first() == '[' && cleanInput.last() == ']' ->
                this.deserializeArray(cleanInput)

            cleanInput.first() == '{' && cleanInput.last() == '}' ->
                this.deserializeObject(cleanInput)

            cleanInput == "null" -> JSONNull

            else -> 
                throw IllegalArgumentException("Invalid JSON input: $input")
        }
    }

    fun deserializeNumber(input: String): JSONNumber
    fun deserializeString(input: String): JSONString
    fun deserializeBoolean(input: String): JSONBoolean
    fun deserializeArray(input: String): JSONArray
    fun deserializeObject(input: String): JSONObject
}