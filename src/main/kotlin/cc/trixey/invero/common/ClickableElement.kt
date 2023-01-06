package cc.trixey.invero.common

import cc.trixey.invero.common.event.WindowClickEvent

/**
 * @author Arasple
 * @since 2023/1/5 14:27
 */
interface ClickableElement : Element {

    var handler: (WindowClickEvent, ClickableElement) -> Unit

    fun onClick(event: WindowClickEvent.(element: ClickableElement) -> Unit): ClickableElement {
        handler = event
        return this
    }

    fun passClickEvent(e: WindowClickEvent) {
        e.isCancelled = true

        handler(e, this)
    }

}