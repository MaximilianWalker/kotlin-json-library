package inference

import core.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.internal.impl.resolve.constants.NullValue

object JSONInference {
    fun ignoreProperty(parameter: KProperty<*>): Boolean {
        return parameter.annotations.filterIsInstance<JSONIgnore>().isNotEmpty()
    }

    fun getPropertyName(parameter: KProperty<*>): String? {
        return parameter.annotations.filterIsInstance<JSONProperty>().firstOrNull()?.name
    }

    fun getPropertyAlias(parameter: KProperty<*>): List<String> {
        return parameter.annotations.filterIsInstance<JSONAlias>().firstOrNull()?.names?.toList() ?: emptyList()
    }

    inline fun <reified T : Any> convertTo(input: JSONElement<*>): T {
        return convertTo(input, typeOf<T>()) as T
    }

    fun convertTo(input: JSONElement<*>, targetType: KType): Any {
        return when (input) {
            is JSONNumber -> convertNumberTo(input, targetType)
            is JSONString -> convertStringTo(input, targetType)
            is JSONBoolean -> convertBooleanTo(input, targetType)
            is JSONObject -> convertObjectTo(input, targetType)
            is JSONArray -> convertArrayTo(input, targetType)
            is JSONNull -> convertNullTo(input, targetType)
        }
    }

    fun convertNumberTo(input: JSONNumber, targetType: KType): Number {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for number conversion")
        return when (targetClass) {
            Int::class -> input.element.toInt()
            Long::class -> input.element.toLong()
            Float::class -> input.element.toFloat()
            Double::class -> input.element.toDouble()
            Short::class -> input.element.toShort()
            Byte::class -> input.element.toByte()
            else -> throw IllegalArgumentException("Cannot convert number to ${targetClass.simpleName}")
        }
    }

    fun convertStringTo(input: JSONString, targetType: KType): Any {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for string conversion")
        return when {
            targetClass.isSubclassOf(Enum::class) -> {
                val enumConstants = targetClass.java.enumConstants
                enumConstants?.firstOrNull { it.toString() == input.element }
                    ?: throw IllegalArgumentException("Cannot convert string to enum ${targetClass.simpleName}")
            }

            targetClass == String::class -> input.element
            else -> throw IllegalArgumentException("Cannot convert string to ${targetClass.simpleName}")
        }
    }

    fun convertBooleanTo(input: JSONBoolean, targetType: KType): Boolean {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for boolean conversion")
        return when (targetClass) {
            Boolean::class -> input.element
            else -> throw IllegalArgumentException("Cannot convert boolean to ${targetClass.simpleName}")
        }
    }

    fun convertArrayTo(input: JSONArray, targetType: KType): List<*> {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for array conversion")

        if (!targetClass.isSubclassOf(List::class)) {
            throw IllegalArgumentException("Input is JSONArray, but target type is not a List: ${targetType}. Expected List<...>.")
        }

        val elementType = targetType.arguments.firstOrNull()?.type
            ?: throw IllegalArgumentException("Could not determine element type for List: $targetType")

        val elementTypeClass = elementType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Could not find any class for element type List")

        @Suppress("UNCHECKED_CAST")
        return input.map { element ->
            try {
                convertTo(element, elementType)
            } catch (e: Exception) {
                throw IllegalArgumentException(
                    "Error converting element in JSONArray to ${elementTypeClass.simpleName}: ${e.message}",
                    e
                )
            }
        }
    }

    fun convertObjectTo(input: JSONObject, targetType: KType): Any {
        val targetClass = targetType.classifier as? KClass<*>
            ?: throw IllegalArgumentException("Invalid target type for object conversion")

        require(targetClass.isData) { "The class must be a data class." }

        val constructor = targetClass.primaryConstructor
        val args = mutableMapOf<KParameter, Any?>()

        constructor.parameters.forEach { parameter ->
            val property = targetClass.getProperty(parameter.name!!)
            val ignored = ignoreProperty(property)

            if (ignored && !property.returnType.isMarkedNullable) {
                throw IllegalArgumentException("Cannot ignore non-nullable property '${property.name}'")
            } else if (ignoreProperty(property)) {
                args[parameter] = null
                return@forEach
            }

            val name = getPropertyName(property) ?: parameter.name
            val aliases = getPropertyAlias(property)

            val value = when {
                name != null && input.contains(name) -> input[name]
                aliases.any { input.contains(it) } -> aliases.firstNotNullOfOrNull { input[it] }
                else -> null
            }

            if (value == null && !property.returnType.isMarkedNullable) {
                throw IllegalArgumentException("Required property '${property.name}' is missing in the input JSON.")
            }

            args[parameter] = value?.let { jsonElement ->
                val paramType = property.returnType
                val convertedValue = convertTo(jsonElement, paramType)

                val propertyClass = paramType.classifier as? KClass<*>
                    ?: throw IllegalArgumentException("Invalid type for property '${property.name}'")

                if (!propertyClass.isInstance(convertedValue)) {
                    throw IllegalArgumentException(
                        "Type mismatch for property '${property.name}'. " +
                                "Expected ${propertyClass.simpleName}, got ${convertedValue::class.simpleName}"
                    )
                }

                convertedValue
            }
        }

        return constructor.callBy(args)
    }

    fun convertNullTo(input: JSONNull, targetType: KType): NullValue {
        return NullValue()
    }

    fun <T> convertFrom(input: T): JSONElement<*> {
        return JSONNull
    }
}