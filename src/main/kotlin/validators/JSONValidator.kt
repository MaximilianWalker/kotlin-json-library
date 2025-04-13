package validators

import core.JSONElement

interface JSONValidator<T> where T : JSONElement<*> {
    fun isValid(element: T) : Boolean
}