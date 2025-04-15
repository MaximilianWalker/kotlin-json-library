package inference

@Target(AnnotationTarget.PROPERTY)
annotation class JSONProperty(val name: String = "", val required: Boolean = false)