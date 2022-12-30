package cc.trixey.invero.panel

import cc.trixey.invero.common.*

/**
 * @author Arasple
 * @since 2022/12/29 13:48
 */
class FreeformPanel(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Pair<Int, Int>,
    locate: Pos
) : PanelInstance(parent, weight, scale, locate), ElementalPanel {

    private val elements: ElementMap = ElementMap()

    override fun getElemap(): ElementMap {
        return elements
    }

    var viewport: Pair<Int, Int> = 0 to 0

    fun setViewport(x: Int, y: Int) {
        viewport = x to y
    }



}