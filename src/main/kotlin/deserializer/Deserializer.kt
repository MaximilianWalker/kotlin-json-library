package deserializer

interface Deserializer<TInput, TOutput> {
    fun deserialize(input: TInput): TOutput
}