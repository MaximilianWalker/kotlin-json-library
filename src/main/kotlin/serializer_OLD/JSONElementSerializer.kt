package serializer_OLD

import core.JSONElement
import java.lang.reflect.ParameterizedType
import org.reflections.Reflections
import kotlin.reflect.KClass

interface JSONElementSerializer<T> : TextSerializer<T> where T : JSONElement<*> {
    companion object {
        fun findSerializerFor(target: JSONElement<*>): KClass<*>? {
            val allSerializers = listOf(
                JSONArraySerializer::class
                // Add more here or use a classpath scanning library
            )

            val currentPackage = object {}.javaClass.`package`.name
            val reflections = Reflections(currentPackage)

            return allSerializers.find { kclass ->
                val superType = kclass.java.genericSuperclass
                if (superType is ParameterizedType) {
                    val typeArg = superType.actualTypeArguments.firstOrNull()
                    typeArg == target
                } else false
            }
        }
    }

    fun serialize
}