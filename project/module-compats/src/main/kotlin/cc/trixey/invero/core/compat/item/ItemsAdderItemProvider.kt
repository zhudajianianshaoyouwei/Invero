package cc.trixey.invero.core.compat.item

import cc.trixey.invero.common.supplier.ItemSourceProvider
import cc.trixey.invero.core.compat.PluginHook
import dev.lone.itemsadder.api.CustomStack
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.ItemsAdderItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:50
 */
class ItemsAdderItemProvider : ItemSourceProvider, PluginHook() {

    override val pluginName = "ItemsAdder"

    override fun getItem(identifier: String): ItemStack? {
        return CustomStack.getInstance(identifier)?.itemStack
    }

}