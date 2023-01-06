package cc.trixey.invero.common

import cc.trixey.invero.common.event.WindowClickEvent

/**
 * @author Arasple
 * @since 2023/1/5 14:27
 */
interface Clickable : Element {

    fun getHandler(): (WindowClickEvent, Clickable) -> Unit

    fun setHandler(handler: (WindowClickEvent, Clickable) -> Unit)

    fun onClick(event: WindowClickEvent.(element: Clickable) -> Unit): Clickable {
        setHandler(event)
        return this
    }

    fun passClickEvent(e: WindowClickEvent) {
        getHandler()(e, this)
    }

}