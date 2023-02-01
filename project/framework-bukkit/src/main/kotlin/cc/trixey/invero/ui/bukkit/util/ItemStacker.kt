package cc.trixey.invero.ui.bukkit.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.platform.util.isAir

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.util.ItemStacker
 *
 * @author Arasple
 * @since 2023/1/12 13:57
 */
fun ItemStack.reachedMaxStackSize(): Boolean {
    return amount >= maxStackSize
}