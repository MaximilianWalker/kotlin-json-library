package serializer

interface Deserializer<TInput, TOutput> {
    fun serialize(input: TInput): TOutput
}