package cc.trixey.invero.core.compat.item

import cc.trixey.invero.core.compat.PluginHook
import cc.trixey.invero.core.item.source.Provider
import io.th0rgal.oraxen.api.OraxenItems
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.OraxenItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:41
 */
class OraxenItemProvider : Provider, PluginHook() {

    override val pluginName = "Oraxen"

    override fun getItem(identifier: String): ItemStack {
        return OraxenItems.getItemById(identifier).build()
    }

}