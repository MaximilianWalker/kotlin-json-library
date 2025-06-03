package serializer

interface Serializer<TInput, TOutput, TOptions> {
    fun serialize(input: TInput): TOutput
    fun serializeWithOptions(input: TInput, options: TOptions): TOutput
}