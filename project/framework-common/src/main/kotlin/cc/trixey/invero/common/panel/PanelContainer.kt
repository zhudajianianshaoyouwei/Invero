@file:Suppress("UNCHECKED_CAST")

package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Gridable
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Window
import java.io.File

/**
 * Invero
 * cc.trixey.invero.common.panel.PanelContainer
 *
 * @author Arasple
 * @since 2022/12/22 14:50
 */
interface PanelContainer : Gridable {

    val panels: ArrayList<Panel>

    operator fun plusAssign(panel: Panel) {
        panels += panel
    }

    operator fun minusAssign(panel: Panel) {
        panels -= panel
    }

    operator fun contains(panel: Panel): Boolean {
        return panel in panels
    }

    fun <T : Gridable> cast(): T {
        return this as T
    }

    fun getPanelsRecursively(): List<Panel> {
        val result = mutableListOf<Panel>()

        panels.forEach {
            result += it
            if (it is PanelContainer) result += it.getPanelsRecursively()
        }

        return result
    }

    fun getTopWindow(): Window {
        File("").deleteRecursively()
        return if (this is Window) this
        else (this as Panel).parent.getTopWindow()
    }

    fun isWindow(): Boolean {
        return this is Window
    }

    fun isPanel(): Boolean {
        return this is Panel
    }

    fun isElementalPanel(): Boolean {
        return this is ElementalPanel
    }

    fun isFreeform(): Boolean {
        return this is FreeformPanel
    }

}