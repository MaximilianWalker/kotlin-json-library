package validators

import core.JSONArray
import core.JSONNull

class JSONArrayTypesValidator : JSONValidator<JSONArray> {
    override fun isValid(element: JSONArray): Boolean {
        if (element.isEmpty()) return true
        val first = element.first()

        if (first == JSONNull) return false

        val type = first::class
        for (item in element) {
            if (item::class != type) return false
        }
        return true
    }
}