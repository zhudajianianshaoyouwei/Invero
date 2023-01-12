package cc.trixey.invero.common.panel

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.ProxyInventory

/**
 * Invero
 * cc.trixey.invero.common.panel.IOPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:46
 *
 * Support Input/Output ItemStack
 */
interface IOPanel : ElementalPanel {

    val positionsInsertable: List<Pos>

    val positionsRetrievable: List<Pos>
        get() = positionsInsertable

    val positionsLocked: List<Pos>

    val inventory: ProxyInventory
        get() = window.inventory

    fun renderStorage()

}