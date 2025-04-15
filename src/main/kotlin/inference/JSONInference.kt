package inference

import core.*
import kotlin.reflect.*
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.internal.impl.resolve.constants.NullValue

object JSONInference {
    fun ignoreParameter(parameter: KParameter): Boolean {
        return parameter.annotations.filterIsInstance<JSONIgnore>().isNotEmpty()
    }

    fun getParameterName(parameter: KParameter): String? {
        return parameter.annotations.filterIsInstance<JSONProperty>().firstOrNull()?.name
    }

    fun isParameterRequired(parameter: KParameter): Boolean {
        return parameter.annotations.filterIsInstance<JSONProperty>().firstOrNull()?.required ?: false
    }

    fun getParameterAlias(parameter: KParameter): List<String> {
        return parameter.annotations.filterIsInstance<JSONAlias>().firstOrNull()?.names?.toList() ?: emptyList()
    }

    inline fun <reified T : Any> convertTo(input: JSONElement<*>): T {
        return when (input) {
            is JSONNumber -> convertNumberTo(input)
            is JSONString -> convertStringTo(input)
            is JSONBoolean -> convertBooleanTo(input)
            is JSONObject -> convertObjectTo(input, this::convertTo)
            is JSONArray -> convertArrayTo(input, this::convertTo)
            is JSONNull -> convertNullTo(input)
        }
    }

    inline fun <reified T : Any> convertNumberTo(input: JSONNumber): T {
        return when (T::class) {
            Int::class -> input.element.toInt() as T
            Long::class -> input.element.toLong() as T
            Float::class -> input.element.toFloat() as T
            Double::class -> input.element.toDouble() as T
            Short::class -> input.element.toShort() as T
            Byte::class -> input.element.toByte() as T
            else -> throw IllegalArgumentException("Cannot convert number to ${T::class.simpleName}")
        }
    }

    inline fun <reified T : Any> convertStringTo(input: JSONString): T {
        return when {
            T::class.isSubclassOf(Enum::class) -> {
                val enumConstants = T::class.java.enumConstants
                enumConstants?.firstOrNull { it.toString() == input.element } as? T
                    ?: throw IllegalArgumentException("Cannot convert string to enum ${T::class.simpleName}")
            }

            T::class == String::class -> input.element as T
            else -> throw IllegalArgumentException("Cannot convert string to ${T::class.simpleName}")
        }
    }

    inline fun <reified T : Any> convertBooleanTo(input: JSONBoolean): T {
        return when (T::class) {
            Boolean::class -> input.element as T
            else -> throw IllegalArgumentException("Cannot convert boolean to ${T::class.simpleName}")
        }
    }

    inline fun <reified T : Any> convertArrayTo(input: JSONArray, converter: (JSONElement<*>) -> Any?): T {
        if (!T::class.isSubclassOf(List::class)) {
            throw IllegalArgumentException("Input is JSONArray, but target type is not a List: ${T::class.simpleName}. Expected List<...>.")
        }

        val type = typeOf<T>().arguments.firstOrNull()?.type
        val clazz = type?.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Could not determine element type for List: ${T::class.simpleName}. Ensure List has a concrete type argument.")

        return input.map<List<*>> {
            try {
                converter(it)
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Error converting element in JSONArray to ${clazz.simpleName}: ${e.message}",
                    e
                )
            }
        } as T
    }

    inline fun <reified T : Any> convertObjectTo(input: JSONObject, converter: (JSONElement<*>) -> Any?): T {
        require(T::class.isData) { "The class must be a data class." }

        val constructor = T::class.primaryConstructor
        val args = mutableMapOf<KParameter, Any?>()

        constructor.parameters.forEach { parameter ->
            if (ignoreParameter(parameter)) return@forEach

            val name = getParameterName(parameter) ?: parameter.name
            val isRequired = isParameterRequired(parameter)
            val aliases = getParameterAlias(parameter)

            val value = when {
                name != null && input.contains(name) -> input[name]
                aliases.any { input.contains(it) } -> aliases.firstNotNullOfOrNull { input[it] }
                else -> null
            }

            if (value == null && isRequired) {
                throw IllegalArgumentException("Required parameter '${parameter.name}' is missing in the input JSON.")
            }

            args[parameter] = value?.let { converter(it) }
        }

        return constructor.callBy(args)
    }

    inline fun <reified T : Any> convertNullTo(input: JSONNull): T {
        return NullValue() as T
    }

    fun <T> convertFrom(input: T): JSONElement<*> {
        return JSONNull
    }
}