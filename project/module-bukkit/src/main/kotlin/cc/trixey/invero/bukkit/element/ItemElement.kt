package cc.trixey.invero.bukkit.element

import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.common.*
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.util.locatingAbsoluteSlot
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import java.util.function.Supplier

/**
 * Invero
 * cc.trixey.invero.bukkit.element.ItemElement
 *
 * @author Arasple
 * @since 2022/12/29 22:38
 */
abstract class ItemElement(
    override val panel: Panel,
    value: ItemStack = ItemStack(Material.STONE)
) : Supplier<ItemStack>, Element, Clickable {

    private var handler: (WindowClickEvent, Clickable) -> Unit = { _, _ -> }

    internal var value: ItemStack = value
        set(value) {
            field = value
            safePush()
        }


    override fun getHandler(): (WindowClickEvent, Clickable) -> Unit {
        return handler
    }

    override fun setHandler(handler: (WindowClickEvent, Clickable) -> Unit) {
        this.handler = handler
    }

    fun modify(builder: ItemBuilder.() -> Unit) {
        value = buildItem(value, builder)
    }

    fun build(material: Material, builder: ItemBuilder.() -> Unit = {}) {
        value = buildItem(material, builder)
    }

    fun buildAsync(supplier: Supplier<ItemStack>) {
        submitAsync { value = supplier.get() }
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

    abstract fun get(viewer: Viewer): ItemStack

    override fun get() = value

}