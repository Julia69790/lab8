package component

import data.*
import hoc.withDisplayName
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import kotlin.browser.document
import kotlin.reflect.KClass

interface AppProps : RProps {
}

interface AppState : RState {
    var presents: Array<Array<Boolean>>
    var lessons: Array<Lesson>
    var students: Array<Student>
}

interface RouteNumberResult : RProps {
    var number: String
}

class App : RComponent<AppProps, AppState>() {
    override fun componentWillMount() {
        state.lessons = lessonsList
        state.students = studentList
        state.presents = Array(state.lessons.size) {
            Array(state.students.size) { false }
        }

    }

    override fun RBuilder.render() {
        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li{ navLink("/editL"){+"Edit lesson"} }
                    li{ navLink("/editS"){+"Edit student"} }
                }
            }
        }

        switch {
            route("/lessons",
                exact = true,
                render = {
                    anyList(state.lessons, "Lessons", "/lessons")
                }
            )
            route("/students",
                exact = true,
                render = {
                    anyList(state.students, "Students", "/students")
                }
            )
            route("/editL",
                exact = true,
                render = {
                        editElement(
                            RBuilder::displayLesson,
                            RBuilder::editLesson,
                            state.lessons,
                            addLesson(),
                            state.lessons.mapIndexed { index, lesson ->
                                editL(index,lesson)
                            }.toTypedArray(),
                            state.lessons.mapIndexed{index, lesson ->
                                deleteLesson(index)
                            }.toTypedArray()
                        )
                }
            )

            route("/editS",
                exact = true,
                render = {
                    editElement(
                        RBuilder::displayStudent,
                        RBuilder::editStudent,
                        state.students,
                        addStudent(),
                        state.students.mapIndexed { index, student ->
                            editS(index,student)
                        }.toTypedArray(),
                        state.students.mapIndexed { index, student ->
                            deleteStudent(index)
                        }.toTypedArray()
                    )
                }
            )
            route("/lessons/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val lesson = state.lessons.getOrNull(num)
                    if (lesson != null)
                        anyFull(
                            RBuilder::student,
                            lesson,
                            state.students,
                            state.presents[num]
                        ) { onClick(num, it) }
                    else
                        p { +"No such lesson" }
                }
            )
            route("/students/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val student = state.students.getOrNull(num)
                    if (student != null)
                        anyFull(
                            RBuilder::lesson,
                            student,
                            state.lessons,
                            state.presents.map {
                                it[num]
                            }.toTypedArray()
                        ) { onClick(it, num) }
                    else
                        p { +"No such student" }
                }
            )
        }
    }

    fun onClick(indexLesson: Int, indexStudent: Int) =
        { _: Event ->
            setState {
                presents[indexLesson][indexStudent] =
                    !presents[indexLesson][indexStudent]
            }
        }

    fun editL(index:Int,lesson:Lesson) = {_: Event ->
        val input1 = document.getElementById("${lesson.name}") !! as HTMLInputElement
        val correctLesson = Lesson("${input1.value}")
        setState{
            state.lessons[index]= correctLesson
        }
    }

    fun editS(index:Int, student: Student) =  { _: Event ->
        val input1 = document.getElementById("${student.firstname}") !! as HTMLInputElement
        val input2 = document.getElementById("${student.surname}") !! as HTMLInputElement
        val correctStudent = Student("${input1.value}","${input2.value}")
        setState{
            state.students[index]= correctStudent
        }
    }

    fun addLesson()= {_: Event ->
        val myinput =  document.getElementById("add")!! as HTMLInputElement
        val extraLesson = Lesson("${myinput.value}")
        setState {
            lessons += extraLesson
            presents += arrayOf(Array(state.students.size){ false })
        }
    }

    fun addStudent()= {_: Event ->
        val myinput1 =  document.getElementById("add")!! as HTMLInputElement
        val myinput2 = document.getElementById("addOnlyStudents")!! as HTMLInputElement
        val newStudent = Student("${myinput1.value}","${myinput2.value}")
        setState{
            students += newStudent
            presents += arrayOf(Array(state.lessons.size){ false })
        }
    }

    fun deleteLesson( index: Int) = {_: Event ->
        var newLessons = state.lessons.slice(0 until index).toTypedArray()
        newLessons += state.lessons.slice(index+1 until state.lessons.size).toTypedArray()
        var newPresents = state.presents[index].slice(0 until index).toTypedArray()
        newPresents += state.presents[index].slice(index+1 until state.students.size).toTypedArray()
        setState{
            lessons = newLessons
            presents[index] = newPresents
        }
    }
    fun deleteStudent(index: Int) = { _: Event ->
        var newStudents = state.students.slice(0 until index).toTypedArray()
        newStudents += state.students.slice(index+1 until state.students.size).toTypedArray()
        var newPresents = state.presents[index].slice(0 until index).toTypedArray()
        newPresents += state.presents[index].slice(index+1 until state.students.size).toTypedArray()
        setState{
            students = newStudents
            presents[index] = newPresents
        }
    }
}

fun RBuilder.app() =
    child(
        withDisplayName("AppHoc", App::class)
    ) {}





