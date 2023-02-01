package cc.trixey.invero.ui.common.panel

import cc.trixey.invero.ui.common.Pos
import cc.trixey.invero.ui.common.ProxyInventory

/**
 * Invero
 * cc.trixey.invero.ui.common.panel.IOPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:46
 *
 * Support Input/Output ItemStack
 */
interface IOPanel : ElementalPanel {

    val insertable: ArrayList<Pos>

    val retrievable: List<Pos>
        get() = insertable

    val locked: ArrayList<Pos>

    val inventory: ProxyInventory
        get() = window.inventory

    fun lock(pos: Pos)

    fun free(pos: Pos)

    fun renderStorage()

    fun hasAvailableSlot(): Boolean

}