package cc.trixey.invero.common.panel

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

    val inventory: ProxyInventory
        get() = window.inventory

}