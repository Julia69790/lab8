package component

import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.functionalComponent
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch

interface AnyListProps<O> : RProps {
    var objs: Array<O>
}

fun <T> fAnyList(name: String, path: String) =
    functionalComponent<AnyListProps<T>> {
        h2 { +name }
        ul {
            it.objs.mapIndexed{ index, obj ->
                li {
                    navLink("$path/$index"){
                        +obj.toString()
                    }
                }
            }
        }
    }

fun <T> RBuilder.anyList(
    anys: Array<T>,
    name: String,
    path: String
) = child(
    withDisplayName(name, fAnyList<T>(name, path))
){
    attrs.objs = anys
}

