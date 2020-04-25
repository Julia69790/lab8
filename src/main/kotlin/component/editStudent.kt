package component

import data.Lesson
import data.Student
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.input
import react.functionalComponent

interface editStudentProps: RProps {
    var student: Student
    var editS:(Event)->Unit
}

val feditStudent =
    functionalComponent<editStudentProps> {
        input(InputType.text){
            attrs.id = "${it.student.firstname}"
        }
        input(InputType.text){
            attrs.id = "${it.student.surname}"
        }
        input(InputType.button){
            attrs.value = "Edit"
            attrs.onClickFunction = it.editS
        }
    }

fun RBuilder.editStudent(
    student: Student,
    editS:(Event)->Unit
) = child(feditStudent){
    attrs.student = student
    attrs.editS = editS
}