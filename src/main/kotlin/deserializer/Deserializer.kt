package deserializer

interface Deserializer<TInput, TOutput> {
    fun serialize(input: TInput): TOutput
}