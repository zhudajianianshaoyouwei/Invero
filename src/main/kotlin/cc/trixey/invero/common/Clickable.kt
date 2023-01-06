package cc.trixey.invero.common

import cc.trixey.invero.common.event.WindowClickEvent

/**
 * @author Arasple
 * @since 2023/1/5 14:27
 */
interface Clickable : Element {

    var handler: (WindowClickEvent, Clickable) -> Unit

    fun onClick(event: WindowClickEvent.(element: Clickable) -> Unit): Clickable {
        handler = event
        return this
    }

    fun passClickEvent(e: WindowClickEvent) {
        handler(e, this)
    }

}