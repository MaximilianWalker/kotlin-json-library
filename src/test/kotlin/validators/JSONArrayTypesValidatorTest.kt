package validators

import core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JSONArrayTypesValidatorTest {

    @Test
    fun `test empty array is valid`() {
        val jsonArray = JSONArray(mutableListOf())
        val validator = JSONArrayTypesValidator()
        assertTrue(validator.isValid(jsonArray))
    }

    @Test
    fun `test array with same types is valid`() {
        val jsonArray = JSONArray(mutableListOf(
            JSONNumber(10),
            JSONNumber(20),
            JSONNumber(30)
        ))
        val validator = JSONArrayTypesValidator()
        assertTrue(validator.isValid(jsonArray))
    }

    @Test
    fun `test array with different types is invalid`() {
        val jsonArray = JSONArray(mutableListOf(
            JSONNumber(10),
            JSONString("Hello"),
            JSONBoolean(true)
        ))
        val validator = JSONArrayTypesValidator()
        assertFalse(validator.isValid(jsonArray))
    }

    @Test
    fun `test array with JSONNull as the first element is invalid`() {
        val jsonArray = JSONArray(mutableListOf(JSONNull, JSONNumber(1)))
        val validator = JSONArrayTypesValidator()
        assertFalse(validator.isValid(jsonArray))
    }

    @Test
    fun `test array with JSONNull later is invalid`() {
        val jsonArray = JSONArray(mutableListOf(
            JSONString("test"),
            JSONNull
        ))
        val validator = JSONArrayTypesValidator()
        assertFalse(validator.isValid(jsonArray))
    }
}