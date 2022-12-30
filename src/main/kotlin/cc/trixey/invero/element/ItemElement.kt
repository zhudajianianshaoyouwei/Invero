package cc.trixey.invero.element

import cc.trixey.invero.common.*
import cc.trixey.invero.panel.ElementalPanel
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
    value: ItemStack = ItemStack(Material.AIR)
) : Supplier<ItemStack>, Element {

    var value = value
        set(value) {
            field = value
            push()
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

    override fun push(parent: PanelContainer) {
        if (panel is ElementalPanel) {
            if (parent.isWindow()) {
                val elemap = (panel as ElementalPanel).getElemap()
                val positions = elemap.find(this) ?: error("Not found position for this itemElement")
                val window = parent.cast<Window>()
                val slots = positions.toLocations().map {
                    val (x, y) = it
                    window.locate(x, y)
                }

                slots.forEach { window.inventory.toBukkitProxy()[it] = value }
            } else if (parent.isElementalPanel()) {
                error("none supported parent")
            } else if (parent.isPanel()) {
                push(parent.cast<Panel>().parent)
            }
        }
    }

    abstract fun get(viewer: Viewer): ItemStack

    override fun get(): ItemStack {
        return value
    }

}