package component

import data.Lesson
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.input
import react.functionalComponent

interface EditLessonProps: RProps {
    var lesson: Lesson
    var editL:(Event)->Unit
}

val feditLesson =
    functionalComponent<EditLessonProps> {
            input(InputType.text){
                attrs.id = "${it.lesson.name}"
            }
            input(InputType.button){
                attrs.value = "Edit"
                attrs.onClickFunction = it.editL
            }
        }


fun RBuilder.editLesson(
    lesson: Lesson,
    editL:(Event)->Unit
) = child(feditLesson){
    attrs.lesson = lesson
    attrs.editL = editL
}