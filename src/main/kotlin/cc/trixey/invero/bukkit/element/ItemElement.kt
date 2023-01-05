package cc.trixey.invero.bukkit.element

import cc.trixey.invero.common.*
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
    override val panel: Panel, internal var value: ItemStack = ItemStack(Material.STONE)
) : Supplier<ItemStack>, Element {

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
        // 元素真实父级 Panel 需要支持容纳元素
        if (panel is ElementalPanel) {
            // 默认 parent = panel.parent，即元素的父级 Panel 的父级
            // 如果其是 Window（理解为顶层，支持进行渲染）
            if (parent.isWindow()) {
                val elemap = (panel as ElementalPanel).getElemap()
                val positions = elemap.find(this) ?: error("Not found position for this itemElement")
                val window = parent.cast<Window>()
                // 向外渲染逻辑：定位槽位
                // TODO 检查性能损耗
                val locating: (Pos) -> Int = { pos ->
                    var previous = panel
                    var destination = previous.parent
                    var result = pos

                    while (destination.isPanel()) {
                        result = pos.advance(previous.scale, destination.scale)

                        previous = destination as Panel
                        destination = previous.parent
                    }
                    result.toSlot(destination.scale)
                }
                positions.values.map(locating).filter { it >= 0 }.forEach {
                    window.inventory.toBukkitProxy()[it] = value
                }
            }
            // 当遇到其是容纳元素的 Panel，则抛错
            // 元素的父级必然是 ElementalPanel, 那么其爷级必然不能是一个 ElementalPanel
            else if (parent.isElementalPanel()) {
                error("not supported parent")
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