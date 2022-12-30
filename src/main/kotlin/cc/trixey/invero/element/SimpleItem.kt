package cc.trixey.invero.element

import cc.trixey.invero.common.ElementStatic
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.panel.ElementalPanel
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @since 2022/12/29 22:37
 */
class SimpleItem(panel: Panel) : ElementStatic, ItemElement(panel) {

    override fun get(viewer: Viewer): ItemStack {
        return value
    }

}