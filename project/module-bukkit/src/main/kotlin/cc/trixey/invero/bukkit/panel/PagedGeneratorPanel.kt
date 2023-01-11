package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.BukkitPanel
import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.element.ItemElement
import cc.trixey.invero.common.Clickable
import cc.trixey.invero.common.Elements
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.GeneratorPanel
import cc.trixey.invero.common.panel.PagedPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

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
) : BukkitPanel(parent, weight, scale, locate), PagedPanel, GeneratorPanel<T, ItemElement> {

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

    override val generatorPool: Set<Pos> by lazy {
        scale.getArea() - elements.occupiedPositions()
    }

    override var sourceElements: List<T> = listOf()

    override val outputElements by lazy {
        ArrayList(arrayOfNulls<ItemElement?>(sourceElements.size).toList())
    }

    override var generator: (T) -> ItemElement? = { null }

    fun generatorElements(block: () -> List<T>) {
        sourceElements = block()
    }

    fun onGenerate(block: (T) -> ItemElement?) {
        generator = block
    }

    override fun render() {
        val fromIndex = pageIndex * generatorPool.size
        val toIndex = (fromIndex + generatorPool.size).coerceAtMost(sourceElements.lastIndex)
        maxPageIndex = sourceElements.size / generatorPool.size

        if (sourceElements.size > fromIndex) {
            val apply = (fromIndex..toIndex).map { index ->
                if (outputElements[index] == null) {
                    outputElements[index] = generator(sourceElements[index])
                }
                outputElements[index]
            }

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

    override fun handleClick(pos: Pos, e: WindowClickEvent) {
        elements.findElement(pos)?.let {
            if (it is Clickable) {
                it.passClickEventHandler(e)
            }
        }
    }

}