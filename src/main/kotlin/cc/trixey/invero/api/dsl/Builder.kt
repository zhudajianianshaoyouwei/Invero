package cc.trixey.invero.api.dsl

import cc.trixey.invero.bukkit.BukkitChestWindow
import cc.trixey.invero.common.PanelContainer
import cc.trixey.invero.common.PanelWeight
import cc.trixey.invero.common.Pos
import cc.trixey.invero.element.SimpleItem
import cc.trixey.invero.panel.ElementalPanel
import cc.trixey.invero.panel.StandardPanel
import org.bukkit.Material

/**
 * @author Arasple
 * @since 2022/12/22 20:28
 */
infix fun Int.pos(y: Int): Pos {
    return if (y < 0) Pos(this)
    else Pos(this, y)
}

fun ElementalPanel.item(
    slot: Int,
    material: Material = Material.STONE,
    block: SimpleItem.() -> Unit
) = item(slot pos -1, material, block)

fun ElementalPanel.item(
    pos: Pos,
    material: Material = Material.STONE,
    block: SimpleItem.() -> Unit
) {
    SimpleItem(this).also(block).also {
        it.build(material)
        getElemap().setElement(pos, it)
    }
}

fun PanelContainer.standardPanel(
    scale: Pair<Int, Int>,
    locate: Pos = firstPanelPos(),
    weight: PanelWeight = PanelWeight.NORMAL,
    block: StandardPanel.() -> Unit
) {
    this += StandardPanel(this, weight, scale, locate).also(block)
}

private fun PanelContainer.firstPanelPos(): Pos {
    return Pos(0)
}

fun pos(slot: Int, y: Int = 0): Pos {
    return if (y <= 0) Pos(slot)
    else Pos(slot, y)
}

fun bukkitChestWindow(rows: Int, title: String, block: BukkitChestWindow.() -> Unit): BukkitChestWindow {
    return BukkitChestWindow(rows, title).also(block)
}