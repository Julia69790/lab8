package data

data class Lesson(
    val name: String
){
    override fun toString(): String = name
}

val lessonsList = arrayOf(
    Lesson("Lecture"),
    Lesson("Practice"),
    Lesson("Exam")
)