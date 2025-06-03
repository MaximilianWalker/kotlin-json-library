package converter

import core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DeserializationTests {

    private val converter = JSONConverter()

    @Test
    fun `test deserializeNumber`() {
        val input = "123"
        val result = converter.deserializeNumber(input)
        assertEquals(JSONNumber(123), result)
    }

    @Test
    fun `test deserializeString`() {
        val input = "\"Hello\""
        val result = converter.deserializeString(input)
        assertEquals(JSONString("Hello"), result)
    }

    @Test
    fun `test deserializeBoolean`() {
        val input = "true"
        val result = converter.deserializeBoolean(input)
        assertEquals(JSONBoolean(true), result)
    }

    @Test
    fun `test deserializeArray`() {
        val input = "[1, 2, 3]"
        val result = converter.deserializeArray(input)
        assertEquals(JSONArray(JSONNumber(1), JSONNumber(2), JSONNumber(3)), result)
    }

    @Test
    fun `test deserializeObject`() {
        val input = "{\"key\":\"value\"}"
        val result = converter.deserializeObject(input)
        assertEquals(JSONObject("key" to JSONString("value")), result)
    }
}