package cc.trixey.invero.bukkit.element

import cc.trixey.invero.common.event.WindowClickEvent

/**
 * Invero
 * cc.trixey.invero.common.Clickable
 *
 * @author Arasple
 * @since 2023/1/5 14:27
 */
interface Clickable<T> {

    fun runHandler(event: WindowClickEvent)

    fun addHandler(handler: (WindowClickEvent, clicked: T) -> Unit)

    fun onClick(event: WindowClickEvent.(clicked: T) -> Unit): T {
        addHandler(event)
        return getInstance()
    }

    fun getInstance(): T


}