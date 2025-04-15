package inference

import core.*
import kotlin.reflect.*
import kotlin.reflect.full.*
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
        return convertTo(input, typeOf<T>())
    }

    fun <T : Any> convertTo(input: JSONElement<*>, targetType: KType): T {
        println(input)
        println(targetType)
        return when (input) {
            is JSONNumber -> convertNumberTo(input, targetType)
            is JSONString -> convertStringTo(input, targetType)
            is JSONBoolean -> convertBooleanTo(input, targetType)
            is JSONObject -> convertObjectTo(input, targetType)
            is JSONArray -> convertArrayTo(input, targetType)
            is JSONNull -> convertNullTo(input, targetType)
        }
    }

    fun <T : Any> convertNumberTo(input: JSONNumber, targetType: KType): T {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for number conversion")
        return when (targetClass) {
            Int::class -> input.element.toInt() as T
            Long::class -> input.element.toLong() as T
            Float::class -> input.element.toFloat() as T
            Double::class -> input.element.toDouble() as T
            Short::class -> input.element.toShort() as T
            Byte::class -> input.element.toByte() as T
            else -> throw IllegalArgumentException("Cannot convert number to ${targetClass.simpleName}")
        }
    }

    fun <T : Any> convertStringTo(input: JSONString, targetType: KType): T {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for string conversion")
        return when {
            targetClass.isSubclassOf(Enum::class) -> {
                val enumConstants = targetClass.java.enumConstants
                enumConstants?.firstOrNull { it.toString() == input.element } as? T
                    ?: throw IllegalArgumentException("Cannot convert string to enum ${targetClass.simpleName}")
            }

            targetClass == String::class -> input.element as T
            else -> throw IllegalArgumentException("Cannot convert string to ${targetClass.simpleName}")
        }
    }

    fun <T : Any> convertBooleanTo(input: JSONBoolean, targetType: KType): T {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for boolean conversion")
        return when (targetClass) {
            Boolean::class -> input.element as T
            else -> throw IllegalArgumentException("Cannot convert boolean to ${targetClass.simpleName}")
        }
    }

    fun <T : Any> convertArrayTo(input: JSONArray, targetType: KType): T {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for array conversion")

        if (!targetClass.isSubclassOf(List::class)) {
            throw IllegalArgumentException("Input is JSONArray, but target type is not a List: ${targetType}. Expected List<...>.")
        }

        val elementType = targetType.arguments.firstOrNull()?.type
            ?: throw IllegalArgumentException("Could not determine element type for List: $targetType")

        val elementTypeClass = elementType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Could not find any class for element type List")

        println(targetClass)
        println(elementTypeClass)

        @Suppress("UNCHECKED_CAST")
        return input.map { element ->
            try {
                println(element)
                println(elementType)
                convertTo(element, elementType)
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Error converting element in JSONArray to ${elementTypeClass.simpleName}: ${e.message}",
                    e
                )
            }
        } as T
    }

    fun <T : Any> convertObjectTo(input: JSONObject, targetType: KType): T {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for object conversion")
        require(targetClass.isData) { "The class must be a data class." }

        val constructor = targetClass.primaryConstructor
            ?: throw IllegalArgumentException("Data class must have a primary constructor")

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

            args[parameter] = value?.let {
                val paramType = parameter.type
                convertTo(it, paramType)
            }
        }

        return constructor.callBy(args) as T
    }

    fun <T : Any> convertNullTo(input: JSONNull, targetType: KType): T {
        return NullValue() as T
    }

    fun <T> convertFrom(input: T): JSONElement<*> {
        return JSONNull
    }
}