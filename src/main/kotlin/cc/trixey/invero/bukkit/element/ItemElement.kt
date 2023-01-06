package cc.trixey.invero.bukkit.element

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

    override var handler: (WindowClickEvent, Clickable) -> Unit = { _, _ -> }

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
            positions.values.map { locatingSlot(it) }.filter { it >= 0 }.forEach {
                window.inventory.toBukkitProxy()[it] = value
            }
        }
//            // 默认 parent = panel.parent，即元素的父级 Panel 的父级
//            // 如果其是 Window（理解为顶层，支持进行渲染）
//            if (parent.isWindow()) {
//                val elemap = (panel as ElementalPanel).getElemap()
//                val positions = elemap.locateElement(this) ?: error("Not found position for this itemElement")
//                val window = parent.cast<Window>()
//                // 向外渲染逻辑：定位槽位
//                positions.values.map { locatingSlot(it) }.filter { it >= 0 }.forEach {
//                    window.inventory.toBukkitProxy()[it] = value
//                }
//            }
//            // 当遇到其是容纳元素的 Panel，则抛错
//            // 元素的父级必然是 ElementalPanel, 那么其爷级必然不能是一个 ElementalPanel
//            else if (parent.isElementalPanel()) {
//                error("not supported parent")
//            } else if (parent.isPanel()) {
//                push(parent.cast<Panel>().parent)
//            }
    }

    abstract fun get(viewer: Viewer): ItemStack

    override fun get(): ItemStack {
        return value
    }

}