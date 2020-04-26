# lab8
## Задание<br>
Доработайте приложение из видеоуроков.Разработайте компоненты, отвечающие за редактирование названия занятия и имени и фамилии студента. 
Разработайте компонент, отвечающий за редактирование списка элементов (с возможностью добавить или удалить элемент). 
В качестве аргументов этому компоненту передаются компоненты для отображения и для редактирования элемента списка.
Добавьте в приложение страницы для редактирования списка студентов и списка занятий. 
## Выполнение<br>
1. Созданы компоненты editLesson и editStudent для редактирования названия занятия и фамилии и имени студента.<br>
Код editLesson:<br>
        
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
        
 Код editStudent:<br>
        
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
        
2. Созданы компоненты displayLesson и displayStudent для отображения уроков и студентов без дополнительных функций.<br>
Код displayLesson:<br>
        
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
        
Код displayStudent:<br>
        
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
        
3. Создан компонент editElement для добавления и удаления элементов, а также включающий в себя редактирование и отображение элементов.<br>
Код editElement:<br>
        
        interface EditElementProps<O>: RProps {
        var elements: Array<O>
        var edit:Array<(Event)->Unit>
        var add:(Event)->Unit
        var delete:Array<(Event)->Unit>
        }
        
        fun <O> fEditElement(
        rComponent1:RBuilder.(O)-> ReactElement,
        rComponent2: RBuilder.(O,(Event)->Unit)-> ReactElement
        ) =
        functionalComponent<EditElementProps<O>>{
        h3{+"Edit"}
        ul{
            val t = Array<Any>(3){0}
            val t1 = Array<Any>(3){0}
            it.elements.mapIndexed { index, element ->
                li{
                    t[index] = rComponent1(element)
                    t1[index] = rComponent2(element,it.edit[index])
                    input(InputType.button){
                        attrs.value = "Delete"
                        attrs.onClickFunction = it.delete[index]
                    }
                }
            }
            input(InputType.text) {
                attrs.id = "add"
            }
            input(InputType.text){
                attrs.id ="addOnlyStudents"
                attrs.placeholder = "Only for Students"
            }
            input(InputType.button) {
                attrs.value = "Add"
                attrs.onClickFunction = it.add
            }
          }
        }

        fun <O> RBuilder.editElement(
        rComponent1: RBuilder.(O)-> ReactElement,
        rComponent2: RBuilder.(O,(Event)->Unit)-> ReactElement,
        elements: Array<O>,
        add:(Event)->Unit,
        edit:Array<(Event)->Unit>,
        delete:Array<(Event)->Unit>
        )= child(
        withDisplayName("Edit", fEditElement<O>(rComponent1,rComponent2))
        ){
        attrs.elements = elements
        attrs.add = add
        attrs.edit = edit
        attrs.delete = delete
        }
        
4. Созданы функции editL и editS для изменения состояния при изменении названия урока или имени и фамилиия студента.<br>
Код editL:<br>
        
        fun editL(index:Int,lesson:Lesson) = {_: Event ->
        val input1 = document.getElementById("${lesson.name}") !! as HTMLInputElement
        val correctLesson = Lesson("${input1.value}")
        setState{
            state.lessons[index]= correctLesson
        }
        }
        
Код editS:<br>
        
        fun editS(index:Int, student: Student) =  { _: Event ->
        val input1 = document.getElementById("${student.firstname}") !! as HTMLInputElement
        val input2 = document.getElementById("${student.surname}") !! as HTMLInputElement
        val correctStudent = Student("${input1.value}","${input2.value}")
        setState{
            state.students[index]= correctStudent
        }
        }
    
5. Созданы функции addLesson и addStudent для изменения состояния при добавлении нового урока или студента.<br>
Код addLesson:<br>
        
        fun addLesson()= {_: Event ->
        val myinput =  document.getElementById("add")!! as HTMLInputElement
        val extraLesson = Lesson("${myinput.value}")
        setState {
            lessons += extraLesson
            presents += arrayOf(Array(state.students.size){ false })
        }
        }
        
Код addStudent:<br>
        
        fun addStudent()= {_: Event ->
        val myinput1 =  document.getElementById("add")!! as HTMLInputElement
        val myinput2 = document.getElementById("addOnlyStudents")!! as HTMLInputElement
        val newStudent = Student("${myinput1.value}","${myinput2.value}")
        setState{
            students += newStudent
            presents += arrayOf(Array(state.lessons.size){ false })
        }
        }
        
6. Созданы функции deleteLesson и deleteStudent для изменения состояния при удалении элементов. Так как используется slice()
и возвращается новый массив, чтобы после удаления не изменились сведения о уже отмеченных студентах, в состоянии также учитывается удаление
из массива со значениями присутствия/отсутствия.<br>
Код deleteLesson:<br>
        
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
        
Код deleteStudent:<br>
        
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
        
7. Прописаны ссылки для страниц редактирования уроков и студентов:<br>
        
         li{ navLink("/editL"){+"Edit lesson"} }
         li{ navLink("/editS"){+"Edit student"} }
         
8. В switch прописаны маршруты и вывод компонентов редактирования на экран:<br>
Для редактирования уроков:<br>
        
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
            
 Для редактирования студентов:<br>
        
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
            
Открыта страница для редактирования уроков:<br>
![](/screen8/editLesson.png)<br>
Добавление урока:<br>
![](/screen8/добавляемУрок.png)<br>
Добавили урок:<br>
![](/screen8/добавилиУрок.png)<br>
Редактирование урока:<br>
![](/screen8/редактируемУрок.png)<br>
Отредактировали урок:<br>
![](/screen8/отредактировалиУрок.png)<br>
Открыта страница для редактирования студентов:<br>
![](/screen8/editStudent.png)<br>
Добавление студента:<br>
![](/screen8/добавляемСтудента.png)<br>
Добавили студента:<br>
![](/screen8/добавилиСтудента.png)<br>
Редактирование студента:<br>
![](/screen8/редактируемСтудента.png)<br>
Отредактировали студента:<br>
![](/screen8/отредактировалиСтудента.png)<br>
Чтобы проверить корретность удаления, отметили одного из студентов присутствующим:<br>
![](/screen8/отметилиПрисутствие.png)<br>
Далее удалили другого студента из списка:<br>
![](/screen8/удалилиСтудента.png)<br>
Проверили, сохранились ли данные о присутствии оставшихся студентов:<br>
![](/screen8/данныеНеизменны.png)<br>
    
   
    
        
        
        
        
        
    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
