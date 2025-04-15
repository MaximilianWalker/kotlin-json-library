package inference

import kotlin.reflect.*

val <T : Any> KClass<T>.primaryConstructor: KFunction<T>
    get() {
        return constructors.maxByOrNull { it.parameters.size } as? KFunction<T>
            ?: throw NoSuchElementException("No suitable primary constructor found for ${this.simpleName}")
    }

val <T : Any> KClass<T>.properties: List<KProperty<T>>
    get() {
        return members.filterIsInstance<KProperty<T>>()
    }

fun <T : Any> KFunction<T>.getParameter(name: String): KParameter {
    return parameters.find { it.name == name }
        ?: throw NoSuchElementException("Parameter '$name' not found in function '${this.name}'.")
}

fun <T : Any> KClass<T>.getProperty(name: String): KProperty<T> {
    return this.properties.find { it.name == name }
        ?: throw NoSuchElementException("Property '$name' not found in class '${this.simpleName}'.")
}