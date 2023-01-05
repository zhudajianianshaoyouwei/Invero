package cc.trixey.invero.bukkit.util

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyMeta

/**
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