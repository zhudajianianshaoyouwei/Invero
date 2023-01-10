package cc.trixey.invero.bukkit.util

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.*

/**
 * Invero
 * cc.trixey.invero.bukkit.util.Item
 *
 * @author Arasple
 * @since 2023/1/5 14:35
 */
private val namespacedKey = NamespacedKey(BukkitPlugin.getInstance(), "invero_slot")

fun ItemStack?.distinguish(slot: Int): ItemStack? {
    return this?.modifyMeta<ItemMeta> {
        persistentDataContainer.set(namespacedKey, PersistentDataType.BYTE, slot.toByte())
    }
}

fun randomItem(builder: ItemBuilder.() -> Unit = {}): ItemStack {
    var itemStack: ItemStack? = null
    while (itemStack.isAir()) {
        itemStack = ItemStack(Material.values().filter { it.isItem || it.isBlock }.random())
    }
    return buildItem(itemStack!!, builder).also {
        it.amount = it.amount.coerceAtMost(it.maxStackSize)
    }
}

fun randomMaterial() = Material.values().filter { (it.isItem || it.isBlock) && it.isNotAir() }.random()