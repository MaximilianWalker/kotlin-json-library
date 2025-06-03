package validators

import core.JSONElement

fun <T : JSONElement<*>> T.validate(validator: JSONValidator<T>): Boolean {
    return validator.isValid(this)
}