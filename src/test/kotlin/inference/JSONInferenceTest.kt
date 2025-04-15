package inference

import core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JSONInferenceTest {

    @Test
    fun `test convertNumberTo with valid conversions`() {
        val jsonNumber = JSONNumber(42)
        assertEquals(42, JSONInference.convertTo<Int>(jsonNumber))
        assertEquals(42L, JSONInference.convertTo<Long>(jsonNumber))
        assertEquals(42.0, JSONInference.convertTo<Double>(jsonNumber))
    }

    @Test
    fun `test convertNumberTo with invalid conversion`() {
        val jsonNumber = JSONNumber(42)
        assertThrows(IllegalArgumentException::class.java) {
            JSONInference.convertTo<String>(jsonNumber)
        }
    }

    @Test
    fun `test convertStringTo with valid string`() {
        val jsonString = JSONString("Hello")
        assertEquals("Hello", JSONInference.convertTo<String>(jsonString))
    }

    @Test
    fun `test convertStringTo with enum`() {
        val jsonString = JSONString("TEST")
        assertEquals(EvalType.TEST, JSONInference.convertTo<EvalType>(jsonString))
    }

    @Test
    fun `test convertStringTo with invalid enum`() {
        val jsonString = JSONString("INVALID")
        assertThrows(IllegalArgumentException::class.java) {
            JSONInference.convertTo<EvalType>(jsonString)
        }
    }

    @Test
    fun `test convertBooleanTo with valid boolean`() {
        val jsonBoolean = JSONBoolean(true)
        assertEquals(true, JSONInference.convertTo<Boolean>(jsonBoolean))
    }

    @Test
    fun `test convertBooleanTo with invalid type`() {
        val jsonBoolean = JSONBoolean(true)
        assertThrows(IllegalArgumentException::class.java) {
            JSONInference.convertTo<String>(jsonBoolean)
        }
    }

    @Test
    fun `test convertArrayTo with valid list`() {
        val jsonArray = JSONArray(JSONNumber(1), JSONNumber(2), JSONNumber(3))
        val result = JSONInference.convertTo<List<Int>>(jsonArray)
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun `test convertArrayTo with invalid list type`() {
        val jsonArray = JSONArray(JSONNumber(1), JSONString("Hello"))
        assertThrows(IllegalArgumentException::class.java) {
            JSONInference.convertTo<List<Int>>(jsonArray)
        }
    }

    @Test
    fun `test convertObjectTo with valid data class`() {
        val jsonObject = JSONObject(
            "name" to JSONString("Math"),
            "credits" to JSONNumber(5),
            "evaluation" to JSONArray(
                JSONObject(
                    "name" to JSONString("Test"),
                    "percentage" to JSONNumber(50.0),
                    "mandatory" to JSONBoolean(true),
                    "type" to JSONString("TEST")
                )
            )
        )
        val result = JSONInference.convertTo<Course>(jsonObject)
        assertEquals("Math", result.name)
        assertEquals(5, result.credits)
        assertEquals(1, result.evaluation.size)
        assertEquals("Test", result.evaluation[0].name)
    }

    @Test
    fun `test convertObjectTo with missing required field`() {
        val jsonObject = JSONObject(
            "credits" to JSONNumber(5)
        )
        assertThrows(IllegalArgumentException::class.java) {
            JSONInference.convertTo<Course>(jsonObject)
        }
    }

    @Test
    fun `test convertNullTo`() {
        val jsonNull = JSONNull
        assertThrows(ClassCastException::class.java) {
            JSONInference.convertTo<String>(jsonNull)
        }
    }
}