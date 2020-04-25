package component

import data.Student
import react.RBuilder
import react.RProps
import react.child
import react.dom.li
import react.functionalComponent

interface displayStudentProps: RProps {
    var student: Student
}

val fdisplayStudent =
    functionalComponent<displayStudentProps> {
        li{
            +"${it.student.firstname} ${it.student.surname}"
        }
    }

fun RBuilder.displayStudent(
    student: Student
) = child(fdisplayStudent){
    attrs.student = student
}