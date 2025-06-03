package deserializer

/**
 * Exception thrown when an error occurs during JSON parsing operations.
 *
 * @param message The detail message describing the parsing error
 * @param cause The underlying cause of this exception
 * @param enableSuppression Whether suppression is enabled or disabled
 * @param writableStackTrace Whether the stack trace should be writable
 */
class JSONParsingException : Exception {
    /**
     * Constructs a new JSON parsing exception with null as its detail message.
     */
    constructor() : super()

    /**
     * Constructs a new JSON parsing exception with the specified detail message.
     * @param message the detail message
     */
    constructor(message: String?) : super(message)

    /**
     * Constructs a new JSON parsing exception with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * Constructs a new JSON parsing exception with the specified cause.
     * @param cause the cause
     */
    constructor(cause: Throwable?) : super(cause)

    /**
     * Advanced constructor with all control parameters.
     */
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)
}