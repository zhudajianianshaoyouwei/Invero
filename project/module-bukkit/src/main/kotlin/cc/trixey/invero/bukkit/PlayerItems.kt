package cc.trixey.invero.bukkit

import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.bukkit.PlayerItems
 *
 * @author Arasple
 * @since 2023/1/6 14:40
 */
@JvmInline
value class PlayerItems(val storage: Array<ItemStack?> = arrayOfNulls(36))