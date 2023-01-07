package cc.trixey.invero.bukkit.element

import cc.trixey.invero.bukkit.ProxyBukkitInventory
import cc.trixey.invero.common.*
import cc.trixey.invero.common.event.WindowClickEvent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submitAsync
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import java.util.function.Supplier

/**
 * @author Arasple
 * @since 2022/12/29 22:38
 */
abstract class ItemElement(
    override val panel: Panel,
    internal var value: ItemStack = ItemStack(Material.STONE)
) : Supplier<ItemStack>, Element, Clickable {

    private var handler: (WindowClickEvent, Clickable) -> Unit = { _, _ -> }

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

    override fun push() {
        if (panel is ElementalPanel) {
            val window = panel.window
            val elemap = (panel as ElementalPanel).getElements()
            val positions = elemap.locateElement(this) ?: error("Not found position for this itemElement")
            // 向外渲染逻辑：定位槽位
            positions.values.forEach {
                val slot = it.locatingSlot()
                if (slot >= 0) {
                    (window.inventory as ProxyBukkitInventory)[slot] = value
                }
            }
        }
    }

    abstract fun get(viewer: Viewer): ItemStack

    override fun get() = value

}