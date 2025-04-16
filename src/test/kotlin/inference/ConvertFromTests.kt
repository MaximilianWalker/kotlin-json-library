package inference

import core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConvertFromTests {

    @Test
    fun `test convertFrom with number`() {
        val number = 42
        val jsonElement = JSONInference.convertFrom(number)
        assertTrue(jsonElement is JSONNumber)
        assertEquals(42, (jsonElement as JSONNumber).element)
    }

    @Test
    fun `test convertFrom with boolean`() {
        val boolean = true
        val jsonElement = JSONInference.convertFrom(boolean)
        assertTrue(jsonElement is JSONBoolean)
        assertEquals(true, (jsonElement as JSONBoolean).element)
    }

    @Test
    fun `test convertFrom with string`() {
        val string = "Hello"
        val jsonElement = JSONInference.convertFrom(string)
        assertTrue(jsonElement is JSONString)
        assertEquals("Hello", (jsonElement as JSONString).element)
    }

    @Test
    fun `test convertFrom with null`() {
        val jsonElement = JSONInference.convertFrom(null)
        assertTrue(jsonElement is JSONNull)
    }

    @Test
    fun `test convertFrom with list`() {
        val list = listOf(1, 2, 3)
        val jsonElement = JSONInference.convertFrom(list)
        assertTrue(jsonElement is JSONArray)
        val jsonArray = jsonElement as JSONArray
        assertEquals(3, jsonArray.element.size)
        assertTrue(jsonArray.element[0] is JSONNumber)
        assertEquals(1, (jsonArray.element[0] as JSONNumber).element)
    }

    @Test
    fun `test convertFrom with map`() {
        val map = mapOf("key1" to 1, "key2" to "value")
        val jsonElement = JSONInference.convertFrom(map)
        assertTrue(jsonElement is JSONObject)
        val jsonObject = jsonElement as JSONObject
        assertTrue(jsonObject.contains("key1"))
        assertTrue(jsonObject.contains("key2"))
        assertEquals(1, (jsonObject["key1"] as JSONNumber).element)
        assertEquals("value", (jsonObject["key2"] as JSONString).element)
    }

    @Test
    fun `test convertFrom with enum`() {
        val enumValue = EvalType.TEST
        val jsonElement = JSONInference.convertFrom(enumValue)
        assertTrue(jsonElement is JSONString)
        assertEquals("TEST", (jsonElement as JSONString).element)
    }

    @Test
    fun `test convertFrom with data class`() {
        val course = Course("Math", 5, listOf(EvalItem("Exam", 50.0, true, EvalType.EXAM)))
        val jsonElement = JSONInference.convertFrom(course)
        assertTrue(jsonElement is JSONObject)
        val jsonObject = jsonElement as JSONObject
        assertEquals("Math", (jsonObject["name"] as JSONString).element)
        assertEquals(5, (jsonObject["credits"] as JSONNumber).element)
        val evaluationArray = jsonObject["evaluation"] as JSONArray
        assertEquals(1, evaluationArray.element.size)
        val evalItemObject = evaluationArray.element[0] as JSONObject
        assertEquals("Exam", (evalItemObject["name"] as JSONString).element)
        assertEquals(50.0, (evalItemObject["percentage"] as JSONNumber).element)
        assertEquals(true, (evalItemObject["mandatory"] as JSONBoolean).element)
        assertEquals("EXAM", (evalItemObject["type"] as JSONString).element)
    }
}