package inference

data class AnnotatedCourse(
    @JSONProperty(name = "course_name")
    val name: String,

    @JSONProperty(name = "course_credits")
    val credits: Int,

    @JSONIgnore
    val internalCode: String?,

    @JSONAlias("evaluationItem", "assessmentItem")
    val evaluation: List<AnnotatedEvalItem>
)

data class AnnotatedEvalItem(
    @JSONProperty(name = "item_name")
    val name: String,

    @JSONProperty(name = "item_percentage")
    val percentage: Double,

    @JSONProperty(name = "is_mandatory")
    val mandatory: Boolean,

    @JSONProperty(name = "evaluation_type")
    val type: EvalType?
)