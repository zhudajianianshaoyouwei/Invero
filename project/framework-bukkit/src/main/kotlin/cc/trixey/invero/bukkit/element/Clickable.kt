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

    fun runHandler(event: WindowClickEvent): Boolean

    fun addHandler(handler: (WindowClickEvent, clicked: T) -> Any)

    fun onClick(handler: WindowClickEvent.(clicked: T) -> Any): T {
        addHandler(handler)
        return getInstance()
    }

    fun getInstance(): T


}