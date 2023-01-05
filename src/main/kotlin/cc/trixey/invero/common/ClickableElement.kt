package cc.trixey.invero.common

import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2023/1/5 14:27
 */
interface ClickableElement : Element {

    var handler: (InventoryClickEvent, ClickableElement) -> Unit

    fun onClick(event: InventoryClickEvent.(element: ClickableElement) -> Unit): ClickableElement {
        handler = event
        return this
    }

    fun passClickEvent(e: InventoryClickEvent) {
        e.isCancelled = true

        handler(e, this)
    }

}