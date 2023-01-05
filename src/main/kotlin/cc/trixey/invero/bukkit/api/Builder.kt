package cc.trixey.invero.bukkit.api

import cc.trixey.invero.bukkit.element.SimpleItem
import cc.trixey.invero.bukkit.panel.StandardPanel
import cc.trixey.invero.bukkit.window.BukkitChestWindow
import cc.trixey.invero.common.*
import org.bukkit.Material

/**
 * @author Arasple
 * @since 2022/12/22 20:28
 */
fun ElementalPanel.item(
    slot: Int,
    material: Material = Material.STONE,
    block: SimpleItem.() -> Unit
) = item(scale.toPosition(slot), material, block)

fun ElementalPanel.item(
    pos: Pair<Int, Int>,
    material: Material = Material.STONE,
    block: SimpleItem.() -> Unit
) {
    SimpleItem(this).also {
        it.build(material)
        block(it)
        getElemap().addElement(it, Pos(pos))
    }
}

fun PanelContainer.standardPanel(
    scale: Pair<Int, Int>,
    locate: Pair<Int, Int>,
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) {
    this += StandardPanel(this, weight, Scale(scale), Pos(locate)).also(block)
}

fun bukkitChestWindow(rows: Int, title: String, block: BukkitChestWindow.() -> Unit): BukkitChestWindow {
    return BukkitChestWindow(rows, title).also(block)
}