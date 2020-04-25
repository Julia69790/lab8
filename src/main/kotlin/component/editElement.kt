package component

import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.h3
import react.dom.input
import react.dom.li
import react.dom.ul

interface EditElementProps<O>: RProps {
    var elements: Array<O>
    var edit:Array<(Event)->Unit>
   // var present: Array<Boolean>
   // var onClick: (Int)->(Event)->Unit
    var add:(Event)->Unit
    var delete:Array<(Event)->Unit>
}

fun <O> fEditElement(
   // rComponent1: RBuilder.(O,Boolean, (Event) -> Unit) -> ReactElement,
    rComponent1:RBuilder.(O)-> ReactElement,
    rComponent2: RBuilder.(O,(Event)->Unit)-> ReactElement
    //rComponent2:RBuilder.(O)-> ReactElement
) =
    functionalComponent<EditElementProps<O>>{
        h3{+"Edit"}
        ul{
            val t = Array<Any>(/*it.elements.size*/3){0}
            val t1 = Array<Any>(/*it.elements.size*/3){0}
            it.elements.mapIndexed { index, element ->
                li{
                    //t[index] = rComponent1(element, it.present[index],it.onClick(index))
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
    //rComponent1: RBuilder.(O,Boolean, (Event) -> Unit) -> ReactElement,
    rComponent1: RBuilder.(O)-> ReactElement,
    rComponent2: RBuilder.(O,(Event)->Unit)-> ReactElement,
   // rComponent2:RBuilder.(O)-> ReactElement,
    elements: Array<O>,
    //present: Array<Boolean>,
    add:(Event)->Unit,
    edit:Array<(Event)->Unit>,
    delete:Array<(Event)->Unit>//,
   // onClick: (Int)->(Event)->Unit
)= child(
    withDisplayName("EditLess", fEditElement<O>(rComponent1,rComponent2))
){
    attrs.elements = elements
   // attrs.present = present
    attrs.add = add
    attrs.edit = edit
    attrs.delete = delete
   // attrs.onClick = onClick
}