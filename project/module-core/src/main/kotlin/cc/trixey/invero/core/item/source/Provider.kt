package cc.trixey.invero.core.item.source

import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.item.source.Provider
 *
 * @author Arasple
 * @since 2023/1/29 15:36
 */
interface Provider {

    fun getItem(identifier: String): ItemStack?

}