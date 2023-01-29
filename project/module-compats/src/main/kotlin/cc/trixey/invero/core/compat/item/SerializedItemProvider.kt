package cc.trixey.invero.core.compat.item

import cc.trixey.invero.core.item.source.Provider
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.deserializeToItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.compat.item.SerializedItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:45
 */
class SerializedItemProvider : Provider() {

    private val cache = ConcurrentHashMap<ByteArray, ItemStack>()

    override fun getItem(identifier: String): ItemStack {
        val id = identifier.toByteArray()
        return cache.computeIfAbsent(id) { id.deserializeToItemStack() }
    }

}