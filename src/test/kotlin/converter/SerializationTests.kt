package converter

import core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import serializer.JSONSerializationOptions

class SerializationTests {

    private val converter = JSONConverter()
    private val defaultOptions = JSONSerializationOptions(prettyPrint = false, sortKeys = false, indent = "  ")

    @Test
    fun `test number serialization`() {
        val jsonNumber = JSONNumber(42)
        val result = converter.serialize(jsonNumber)
        assertEquals("42", result)
    }

    @Test
    fun `test boolean serialization`() {
        val jsonBoolTrue = JSONBoolean(true)
        val jsonBoolFalse = JSONBoolean(false)
        assertEquals("true", converter.serialize(jsonBoolTrue))
        assertEquals("false", converter.serialize(jsonBoolFalse))
    }

    @Test
    fun `test string serialization`() {
        val jsonString = JSONString("Hello")
        val result = converter.serialize(jsonString)
        assertEquals("\"Hello\"", result)
    }

    @Test
    fun `test array serialization`() {
        val jsonArray = JSONArray(JSONNumber(1), JSONNumber(2), JSONNumber(3))
        val result = converter.serializeWithOptions(jsonArray, defaultOptions)
        assertEquals("[1,2,3]", result)
    }

    @Test
    fun `test object serialization`() {
        val jsonObject = JSONObject()
        jsonObject["name"] = JSONString("Test")
        jsonObject["age"] = JSONNumber(30)
        val result = converter.serializeWithOptions(jsonObject, defaultOptions)
        // Expected order: "name" then "age" based on insertion
        assertEquals("{\"name\":\"Test\",\"age\":30}", result)
    }

    @Test
    fun `test null serialization`() {
        val result = converter.serialize(JSONNull)
        assertEquals("null", result)
    }

    @Test
    fun `test pretty printing`() {
        val options = JSONSerializationOptions(prettyPrint = true, sortKeys = false, indent = "  ")
        val jsonObject = JSONObject()
        jsonObject["title"] = JSONString("Book")
        jsonObject["price"] = JSONNumber(9.99)
        val result = converter.serializeWithOptions(jsonObject, options)
        assertTrue(result.contains("\n") && result.contains("  "))
    }

    @Test
    fun `test key sorting`() {
        val options = JSONSerializationOptions(prettyPrint = false, sortKeys = true, indent = "  ")
        val jsonObject = JSONObject()
        jsonObject["bKey"] = JSONNumber(2)
        jsonObject["aKey"] = JSONNumber(1)
        val result = converter.serializeWithOptions(jsonObject, options)
        // Keys should be sorted alphabetically: aKey first, then bKey
        assertEquals("""{"aKey":1,"bKey":2}""", result)
    }

    @Test
    fun `test escaping quotes in keys`() {
        val jsonObject = JSONObject("\"key\"" to JSONString("value"))
        val result = converter.serializeWithOptions(jsonObject, defaultOptions)
        assertEquals("""{"\"key\"":"value"}""", result)
    }
}