package inference

@Target(AnnotationTarget.PROPERTY)
annotation class JSONAlias(vararg val names: String)