package cc.trixey.invero.bukkit.element.control

import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.panel.PagedPanel

/**
 * Invero
 * cc.trixey.invero.bukkit.element.control.ControlPageItem
 *
 * @author Arasple
 * @since 2023/1/11 21:09
 */
class ControlPageItem(panel: Panel, private val target: PagedPanel, private val amount: Int = +1) : SimpleItem(panel) {

    init {
        onClick { _, _ -> target.nextPage(amount) }
    }

}