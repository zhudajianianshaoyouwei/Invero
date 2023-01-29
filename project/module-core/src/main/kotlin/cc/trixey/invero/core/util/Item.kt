package cc.trixey.invero.core.util

import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.util.Item
 *
 * @author Arasple
 * @since 2023/1/16 20:34
 */
fun ItemStack.hasName(): Boolean {
    return itemMeta?.hasDisplayName() == true
}

fun ItemStack.getName(): String? {
    return itemMeta?.displayName
}

fun ItemStack.getLore(): List<String>? {
    return itemMeta?.lore
}

fun ItemStack.hasLore(): Boolean {
    return itemMeta?.hasLore() == true
}

fun ItemStack.postName(name: String) = synchronized(this) {
    itemMeta = itemMeta?.also { it.setDisplayName(name) }
}

fun ItemStack.postLore(lore: List<String>) = synchronized(this) {
    itemMeta = itemMeta?.also { it.lore = lore }
}

fun ItemStack.postAmount(amount: Int) = synchronized(this) {
    this.amount = amount
}

fun ItemStack.postModel(model: Int) = synchronized(this) {
    itemMeta = itemMeta?.also { it.setCustomModelData(model) }
}