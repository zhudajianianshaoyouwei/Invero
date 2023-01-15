package cc.trixey.invero.core.item

import cc.trixey.invero.core.menu.MenuSession
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.item.ItemProperties
 *
 * @author Arasple
 * @since 2023/1/15 13:58
 */
interface ItemProperty {

    fun invoke(session: MenuSession, itemStack: ItemStack)

}