package serializer_OLD

interface Serializer<TInput, TOutput> {
    fun serialize(input: TInput): TOutput
}