package inference

import core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConvertToTests {

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

    @Test
    fun `test convertObjectTo with annotated course`() {
        val jsonObject = JSONObject(
            "course_name" to JSONString("Physics"),
            "course_credits" to JSONNumber(4),
            "evaluation" to JSONArray(
                JSONObject(
                    "item_name" to JSONString("Final Exam"),
                    "item_percentage" to JSONNumber(70.0),
                    "is_mandatory" to JSONBoolean(true),
                    "evaluation_type" to JSONString("EXAM")
                )
            )
        )

        val result = JSONInference.convertTo<AnnotatedCourse>(jsonObject)
        assertEquals("Physics", result.name)
        assertEquals(4, result.credits)
        assertEquals(1, result.evaluation.size)
        assertEquals("Final Exam", result.evaluation[0].name)
        assertEquals(70.0, result.evaluation[0].percentage)
        assertTrue(result.evaluation[0].mandatory)
        assertEquals(EvalType.EXAM, result.evaluation[0].type)
    }

    @Test
    fun `test convertObjectTo with missing ignored field`() {
        val jsonObject = JSONObject(
            "course_name" to JSONString("Chemistry"),
            "course_credits" to JSONNumber(3),
            "evaluation" to JSONArray(
                JSONObject(
                    "item_name" to JSONString("Lab Work"),
                    "item_percentage" to JSONNumber(50.0),
                    "is_mandatory" to JSONBoolean(false),
                    "evaluation_type" to JSONString("PROJECT")
                )
            )
        )

        val result = JSONInference.convertTo<AnnotatedCourse>(jsonObject)
        assertEquals("Chemistry", result.name)
        assertEquals(3, result.credits)
        assertEquals(1, result.evaluation.size)
        assertEquals("Lab Work", result.evaluation[0].name)
        assertEquals(50.0, result.evaluation[0].percentage)
        assertFalse(result.evaluation[0].mandatory)
        assertEquals(EvalType.PROJECT, result.evaluation[0].type)
    }
}