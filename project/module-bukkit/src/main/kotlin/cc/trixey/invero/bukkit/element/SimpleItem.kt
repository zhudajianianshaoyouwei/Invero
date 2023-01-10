package cc.trixey.invero.bukkit.element

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Viewer
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.bukkit.element.SimpleItem
 *
 * @author Arasple
 * @since 2022/12/29 22:37
 */
class SimpleItem(panel: Panel) : ItemElement(panel) {

    override fun get(viewer: Viewer): ItemStack {
        return value
    }

}