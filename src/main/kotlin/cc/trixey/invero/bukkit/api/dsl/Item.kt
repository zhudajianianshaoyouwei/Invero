package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.element.SimpleItem
import cc.trixey.invero.common.Element
import cc.trixey.invero.common.ElementalPanel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Positions
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
    getElements().addElement(it, Pos(pos))
}

inline fun ElementalPanel.item(
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = SimpleItem(this).also {
    it.build(material)
    block(it)
}

fun <T : Element> T.getPosition(): Positions? {
    val panel = panel as ElementalPanel
    val elemap = panel.getElements()

    return elemap.locateElement(this)
}

fun <T : Element> T.set(vararg slots: Int): T = set(slots.map { panel.scale.toPos(it) }.toSet())

fun <T : Element> T.set(pos: Pos): T = set(setOf(pos))

fun <T : Element> T.set(pos: Set<Pos>): T {
    val panel = panel as ElementalPanel
    val elemap = panel.getElements()

    // Wipe previous cache
    elemap.setElement(this, Positions(pos.toMutableSet()))
        ?.let {
            panel.wipe(it)
        }

    return this
}

fun <T : Element> T.add(vararg slots: Int): T {
    slots.forEach { add(panel.scale.toPos(it)) }
    return this
}

fun <T : Element> T.add(pos: Pos): T {
    val panel = panel as ElementalPanel
    val elemap = panel.getElements()

    elemap.addElement(this, pos)

    return this
}

fun <T : Element> T.fillup(): T {
    val panel = panel as ElementalPanel
    val elemap = panel.getElements()

    panel.getUnoccupiedPositions()
        .forEach { elemap.addElement(this, it) }

    return this
}