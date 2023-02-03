package cc.trixey.invero.ui.bukkit.panel

import cc.trixey.invero.ui.bukkit.BukkitPanel
import cc.trixey.invero.ui.bukkit.PanelContainer
import cc.trixey.invero.ui.bukkit.api.dsl.ruin
import cc.trixey.invero.ui.bukkit.api.dsl.set
import cc.trixey.invero.ui.bukkit.element.Clickable
import cc.trixey.invero.ui.bukkit.element.item.BaseItem
import cc.trixey.invero.ui.bukkit.util.proceed
import cc.trixey.invero.ui.common.Elements
import cc.trixey.invero.ui.common.Pos
import cc.trixey.invero.ui.common.Scale
import cc.trixey.invero.ui.common.event.ClickType
import cc.trixey.invero.ui.common.panel.GeneratorPanel
import cc.trixey.invero.ui.common.panel.PagedPanel
import cc.trixey.invero.ui.common.panel.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.panel.PagedGeneratorPanel
 *
 * @author Arasple
 * @since 2023/1/11 16:08
 */
class PagedGeneratorPanel<T>(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : BukkitPanel(parent, weight, scale, locate), PagedPanel, GeneratorPanel<T, BaseItem<*>> {

    override var generated: List<T> = emptyList()

    override var currentSource: List<T> = emptyList()

    override var outputGenerator: (T) -> BaseItem<*>? = { null }

    override var outputElements = arrayListOf<BaseItem<*>?>()

    override var generatorPool = emptyList<Pos>()

    override var pageIndex: Int = 0
        set(value) {
            if (value < 0) error("Page index can not be a negative number")
            else if (pageIndex > maxPageIndex) error("Page index is out of bounds $maxPageIndex")
            pageChangeCallback(this, field, value)
            field = value
            render()
        }

    override var maxPageIndex: Int = 0

    override var pageChangeCallback: PagedPanel.(fromPage: Int, toPage: Int) -> Unit = { _, _ -> }

    override val elements = Elements()

    override fun reset() {
        outputElements.clear()
        outputElements = ArrayList(arrayOfNulls<BaseItem<*>?>(currentSource.size).toList())

        elements.value.entries.removeIf {
            it.value.values.any { pos -> pos in generatorPool }.proceed { it.key.ruin() }
        }

        generatorPool = (scale.getArea() - elements.occupiedPositions()).sorted()
    }

    override fun render() {
        val fromIndex = pageIndex * generatorPool.size
        val toIndex = (fromIndex + generatorPool.size).coerceAtMost(currentSource.lastIndex)
        maxPageIndex = currentSource.size / generatorPool.size

        if (currentSource.size > fromIndex) {
            val output = (fromIndex..toIndex).map { getOutput(it) }

            elements.apply {
                generatorPool.forEachIndexed { index, pos ->
                    removeElement(pos)
                    if (index <= output.lastIndex) {
                        output[index]?.set(pos)
                    }
                }
                generatorPool
                    .filter { findElement(it) == null }
                    .let { wipe(it) }
            }
        }

        return super.render()
    }

    override fun handleClick(pos: Pos, clickType: ClickType, e: InventoryClickEvent?): Boolean {
        elements.findElement(pos)?.let {
            if (it is Clickable<*>) {
                it.runClickCallbacks(clickType, e)
                return true
            }
        }
        return false
    }

}