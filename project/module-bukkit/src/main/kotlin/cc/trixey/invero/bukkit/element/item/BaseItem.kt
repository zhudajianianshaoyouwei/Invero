package cc.trixey.invero.bukkit.element.item

import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.bukkit.element.Clickable
import cc.trixey.invero.common.Element
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.util.locatingAbsoluteSlot
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

/**
 * Invero
 * cc.trixey.invero.bukkit.element.item.ItemElement
 *
 * @author Arasple
 * @since 2022/12/29 22:38
 */
abstract class BaseItem<T : Element>(override val panel: Panel) : Supplier<ItemStack>, Element, Clickable<T> {

    abstract var value: ItemStack

    abstract val handlers: MutableSet<(WindowClickEvent, T) -> Unit>

    abstract fun get(viewer: Viewer): ItemStack

    abstract fun modify(builder: ItemBuilder.() -> Unit)

    abstract fun build(material: Material, builder: ItemBuilder.() -> Unit = {})

    abstract fun buildAsync(supplier: Supplier<ItemStack>)

    abstract fun buildFuture(completable: CompletableFuture<ItemStack>, timeout: Long = 200L)

    override fun addHandler(handler: (WindowClickEvent, T) -> Unit) {
        this.handlers += handler
    }

    override fun runHandler(event: WindowClickEvent) {
        this.handlers.forEach { it(event, getInstance()) }
    }

    override fun get(): ItemStack {
        return value
    }

    override fun postRender(block: (Pos) -> Unit) {
        if (!panel.isElementValid(this)) return

        val elements = (panel as ElementalPanel).elements
        val positions = elements.locateElement(this)?.values ?: setOf()

        positions.forEach(block)
    }

    override fun push() {
        val window = panel.window

        postRender {
            val slot = locatingAbsoluteSlot(it, panel)
            if (slot >= 0) {
                (window.inventory as ProxyBukkitInventory)[slot] = value
            }
        }
    }

}