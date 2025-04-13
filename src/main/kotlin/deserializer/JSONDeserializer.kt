package deserializer

import core.JSONElement

interface JSONDeserializer<T> : TextDeserializer<T> where T : JSONElement<*>