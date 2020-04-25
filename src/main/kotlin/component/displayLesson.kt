package component

import data.Lesson
import react.RBuilder
import react.RProps
import react.child
import react.dom.li
import react.functionalComponent

interface displayLessonProps: RProps {
    var lesson: Lesson
}

val fdisplayLesson =
    functionalComponent<displayLessonProps> {
            li{
                +it.lesson.name
            }
    }

fun RBuilder.displayLesson(
    lesson: Lesson
) = child(fdisplayLesson){
    attrs.lesson = lesson
}