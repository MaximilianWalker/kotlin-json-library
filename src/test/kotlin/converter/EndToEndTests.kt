package converter

import core.*
import serializer.JSONSerializationOptions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EndToEndTests {

    private val converter = JSONConverter()

    @Test
    fun `test serialize and deserialize number`() {
        val number = JSONNumber(42)
        val serialized = converter.serializeNumber(number)
        val deserialized = converter.deserializeNumber(serialized)
        assertEquals(number, deserialized)
    }

    @Test
    fun `test serialize and deserialize string`() {
        val string = JSONString("Hello")
        val serialized = converter.serializeString(string)
        val deserialized = converter.deserializeString(serialized)
        assertEquals(string, deserialized)
    }

    @Test
    fun `test serialize and deserialize boolean`() {
        val boolean = JSONBoolean(true)
        val serialized = converter.serializeBoolean(boolean)
        val deserialized = converter.deserializeBoolean(serialized)
        assertEquals(boolean, deserialized)
    }

    @Test
    fun `test serialize and deserialize array`() {
        val array = JSONArray(listOf(JSONNumber(1), JSONNumber(2), JSONNumber(3)))
        val serialized = converter.serializeArray(array, JSONSerializationOptions())
        val deserialized = converter.deserializeArray(serialized)
        assertEquals(array, deserialized)
    }

    @Test
    fun `test serialize and deserialize object`() {
        val obj = JSONObject(mapOf("key" to JSONString("value")))
        val serialized = converter.serializeObject(obj, JSONSerializationOptions())
        val deserialized = converter.deserializeObject(serialized)
        assertEquals(obj, deserialized)
    }
}