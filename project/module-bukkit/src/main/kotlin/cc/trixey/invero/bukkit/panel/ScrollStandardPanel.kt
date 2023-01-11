package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.api.dsl.pos
import cc.trixey.invero.bukkit.element.ItemElement
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.panel.ElementalPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight
import cc.trixey.invero.common.panel.ScrollPanel
import cc.trixey.invero.common.scroll.ScrollColum
import cc.trixey.invero.common.scroll.ScrollDirection
import cc.trixey.invero.common.scroll.ScrollTail

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.ScrollStandardPanel
 *
 * @author Arasple
 * @since 2023/1/11 13:41
 *
 * ScrollStandardPanel 继承自 FreeformStandardPanel，包含 ScrollColum 概念
 * 皆在帮助快速构建自定义滚动逻辑的面板
 *
 * 元素的位置是动态规定的，insertColum 函数内调用 buildItem 即可，不建议对元素的位置有任何操作
 */
class ScrollStandardPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos,
    override val direction: ScrollDirection = ScrollDirection.VERTICAL,
    override val tail: ScrollTail = ScrollTail(scale.height),
) : FreeformStandardPanel(parent, weight, scale, locate), ScrollPanel, ElementalPanel {

    class ColumItems(elements: Array<ItemElement?>) : ScrollColum<ItemElement>(elements)

    private val visibleColumsSize: Int by lazy {
        if (direction.isVertical) scale.height else scale.width
    }

    private val columCapacity: Int by lazy {
        if (direction.isVertical) scale.width else scale.height
    }

    private val colums = mutableListOf<ColumItems>()

    private var columsUpdated: Boolean = false

    override var viewport: Pos = Pos.NIL
        set(value) {
            field = handleViewport(value)
            rerender()
        }

    fun insertColum(block: (index: Int) -> ItemElement?) {
        colums += ColumItems((0 until columCapacity).map(block).toTypedArray())
        columsUpdated = false
    }

    override fun render() {
        postColums()
        super<ElementalPanel>.render()
    }

    private fun postColums() {
        if (columsUpdated) return

        fun applyColum(columIndex: Int, columItems: ColumItems) {
            columItems.elements.forEachIndexed { itemIndex, itemElement ->
                if (itemElement != null) {
                    val position = if (direction.isVertical) itemIndex to columIndex else columIndex to itemIndex
                    elements.addElement(itemElement, Pos(position))
                }
            }
        }

        colums.forEachIndexed { columIndex, columItems -> applyColum(columIndex, columItems) }

        if (tail.isLoop) {
            for (i in 0 until (visibleColumsSize - 1).coerceAtMost(colums.size)) {
                val columIndex = i + colums.size
                val columItems = colums[i]

                applyColum(columIndex, columItems)
            }
        }

        columsUpdated = true
    }

    private fun handleViewport(value: Pos): Pos {
        val (x, y) = value.value
        var index = if (direction.isVertical) y else x

        // loop logic
        if (tail.isLoop) {
            if (index < 0) index += colums.size
            if (index > colums.lastIndex) return Pos.NIL
        }

        // stop end
        val tailSize = tail.size.coerceAtMost(visibleColumsSize)
        val max = colums.size - tailSize

        return if (direction.isVertical) pos(0, index.coerceIn(0..max))
        else pos(index.coerceIn(0..max), 0)
    }

}