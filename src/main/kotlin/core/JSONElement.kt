package core

sealed interface JSONElement<T> {
    val element: T
}