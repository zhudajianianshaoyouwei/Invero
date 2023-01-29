package cc.trixey.invero.core.compat.item

import cc.trixey.invero.core.item.source.Provider
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.HDBItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:52
 */
class HDBItemProvider : Provider() {

    override val pluginName = "HeadDatabase"

    private val headDatabaseAPI by lazy { HeadDatabaseAPI() }

    override fun getItem(identifier: String): ItemStack? {
        return headDatabaseAPI.getItemHead(identifier) ?: headDatabaseAPI.randomHead
    }

}