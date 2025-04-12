package serializer

interface Serializer<TInput, TOutput> {
    fun serialize(input: TInput): TOutput
}