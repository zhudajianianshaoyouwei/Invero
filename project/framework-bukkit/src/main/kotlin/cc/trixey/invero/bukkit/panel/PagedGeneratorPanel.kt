package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.bukkit.PanelContainer
import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.element.Clickable
import cc.trixey.invero.bukkit.element.item.BaseItem
import cc.trixey.invero.common.Elements
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.ClickType
import cc.trixey.invero.common.panel.GeneratorPanel
import cc.trixey.invero.common.panel.PagedPanel
import cc.trixey.invero.common.panel.PanelWeight
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.PagedGeneratorPanel
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

    override var sourceElements: List<T> = listOf()

    override val outputElements by lazy {
        ArrayList(arrayOfNulls<BaseItem<*>?>(sourceElements.size).toList())
    }

    override var generator: (T) -> BaseItem<*>? = { null }

    override val generatorPool: List<Pos> by lazy {
        (scale.getArea() - elements.occupiedPositions()).sorted()
    }

    override var pageIndex: Int = 0
        set(value) {
            if (value < 0) error("Page index can not be a negative number")
            else if (pageIndex > maxPageIndex) error("Page index is out of bounds $maxPageIndex")
            pageChangeCallback(this, field, value)
            field = value
            render()
        }

    override var pageChangeCallback: PagedPanel.(fromPage: Int, toPage: Int) -> Unit = { _, _ -> }

    override var maxPageIndex: Int = 0

    override val elements = Elements()

    override fun render() {
        val fromIndex = pageIndex * generatorPool.size
        val toIndex = (fromIndex + generatorPool.size).coerceAtMost(sourceElements.lastIndex)
        maxPageIndex = sourceElements.size / generatorPool.size

        if (sourceElements.size > fromIndex) {
            val apply = (fromIndex..toIndex).map { getOutput(it) }

            elements.apply {
                generatorPool.forEachIndexed { index, pos ->
                    removeElement(pos)

                    if (index <= apply.lastIndex) {
                        apply[index]?.set(pos)
                    }
                }
            }
            return super.render()
        }
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