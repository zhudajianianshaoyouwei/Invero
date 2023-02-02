package cc.trixey.invero.core.compat.item

import cc.trixey.invero.common.supplier.ItemSourceProvider
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
class SerializedItemProvider : ItemSourceProvider {

    private val cache = ConcurrentHashMap<ByteArray, ItemStack>()

    override fun getItem(identifier: String, context: Any?): ItemStack {
        val id = identifier.toByteArray()
        return cache.computeIfAbsent(id) { id.deserializeToItemStack() }
    }

}