package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Gridable
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Window

/**
 * Invero
 * cc.trixey.invero.common.panel.PanelContainer
 *
 * @author Arasple
 * @since 2022/12/22 14:50
 */
interface PanelContainer : Gridable {

    val panels: ArrayList<Panel>

    fun isWindow() = this is Window

    fun isPanel() = this is Panel

    fun isElementalPanel() = this is ElementalPanel

    fun isFreeform() = this is FreeformPanel

    @Suppress("UNCHECKED_CAST")
    fun <T> cast() = this as T

    operator fun plusAssign(panel: Panel) {
        panels += panel
    }

    operator fun minusAssign(panel: Panel) {
        panels -= panel
    }

    operator fun contains(panel: Panel): Boolean {
        return panel in panels
    }

}

inline fun <reified T:Panel> PanelContainer.isXPanel(): Boolean {
    return this is T
}