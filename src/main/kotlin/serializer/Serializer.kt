package serializer

interface Serializer<TInput, TOutput, TOptions> {
    fun serialize(input: TInput): TOutput
    fun serialize(input: TInput, options: TOptions): TOutput
}