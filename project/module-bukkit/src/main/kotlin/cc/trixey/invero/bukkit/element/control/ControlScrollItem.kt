package cc.trixey.invero.bukkit.element.control

import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.panel.ScrollPanel

/**
 * Invero
 * cc.trixey.invero.bukkit.element.control.ControlPageItem
 *
 * @author Arasple
 * @since 2023/1/11 21:19
 */
class ControlScrollItem(panel: Panel, private val target: ScrollPanel, private val amount: Int = +1) : SimpleItem(panel) {

    init {
        addHandler { _, _ -> target.scroll(amount) }
    }

}