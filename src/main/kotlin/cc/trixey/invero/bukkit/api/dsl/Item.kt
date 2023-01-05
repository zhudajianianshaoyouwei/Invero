package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.element.SimpleItem
import cc.trixey.invero.common.ElementStatic
import cc.trixey.invero.common.ElementalPanel
import cc.trixey.invero.common.Pos
import org.bukkit.Material

/**
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun ElementalPanel.item(
    slot: Int,
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = item(scale.toPosition(slot), material, block)

inline fun ElementalPanel.item(
    pos: Pair<Int, Int>,
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = item(material, block).also {
    getElemap().addElement(it, Pos(pos))
}

inline fun ElementalPanel.item(
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = SimpleItem(this).also {
    it.build(material)
    block(it)
}

fun <T:ElementStatic> T.fillup(): T {
    val panel = panel as ElementalPanel
    val elemap = panel.getElemap()

    panel.getUnoccupiedPositions()
        .forEach {
            elemap.addElement(this, it)
        }

    return this
}