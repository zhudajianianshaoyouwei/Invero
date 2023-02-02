package cc.trixey.invero.core.compat.item

import cc.trixey.invero.common.supplier.ItemSourceProvider
import cc.trixey.invero.core.compat.PluginHook
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.HDBItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:52
 */
class HDBItemProvider : ItemSourceProvider, PluginHook() {

    override val pluginName = "HeadDatabase"

    private val headDatabaseAPI by lazy { HeadDatabaseAPI() }

    override fun getItem(identifier: String, context: Any?): ItemStack? {
        return headDatabaseAPI.getItemHead(identifier) ?: headDatabaseAPI.randomHead
    }

}