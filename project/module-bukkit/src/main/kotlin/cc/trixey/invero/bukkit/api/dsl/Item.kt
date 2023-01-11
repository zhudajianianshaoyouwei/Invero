package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.common.Element
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Positions
import cc.trixey.invero.common.panel.ElementalPanel
import org.bukkit.Material

/**
 * Invero
 * cc.trixey.invero.bukkit.api.dsl.Item
 *
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun ElementalPanel.item(
    slot: Int,
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = item(scale.convertToPosition(slot).value, material, block)

inline fun ElementalPanel.item(
    pos: Pair<Int, Int>,
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = buildItem(material, block).also {
    elements.addElement(it, Pos(pos))
}

inline fun ElementalPanel.item(
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = buildItem(material, block).set(Pos(firstAvailablePositionForElement()))

inline fun ElementalPanel.buildItem(
    material: Material,
    block: SimpleItem.() -> Unit = {}
) = SimpleItem(this).also {
    it.build(material)
    block(it)
}

fun <T : Element> T.getPosition(): Positions? {
    val panel = panel as ElementalPanel
    val elements = panel.elements

    return elements.locateElement(this)
}

fun <T : Element> T.ruin(): Boolean {
    val panel = panel as ElementalPanel
    val elements = panel.elements

    elements.locateElement(this)?.let {
        panel.wipe(it.values)
        elements.removeElement(this)
        return true
    }
    return false
}

fun <T : Element> T.set(vararg slots: Int): T = set(slots.map { panel.scale.convertToPosition(it) }.toSet())

fun <T : Element> T.set(pos: Pos): T = set(setOf(pos))

fun <T : Element> T.set(pos: Set<Pos>): T {
    val panel = panel as ElementalPanel
    val elements = panel.elements

    // Wipe previous cache
    elements.setElement(this, Positions(pos))
        ?.let {
            panel.wipe(it)
        }

    // Auto-push
    safePush()

    return this
}

fun <T : Element> T.add(vararg slots: Int): T {
    slots.forEach { add(panel.scale.convertToPosition(it)) }
    return this
}

fun <T : Element> T.add(pos: Pos): T {
    val panel = panel as ElementalPanel
    val elements = panel.elements

    elements.addElement(this, pos)

    return this
}

fun <T : Element> T.fillup(): T {
    val panel = panel as ElementalPanel
    val elements = panel.elements

    panel
        .getUnoccupiedPositions()
        .forEach {
            elements.addElement(this, it)
        }

    return this
}