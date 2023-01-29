package cc.trixey.invero.core.compat.item

import cc.trixey.invero.core.item.source.Provider
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.ZaphkielItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:44
 */
class ZaphkielItemProvider : Provider() {

    override val pluginName = "Zaphkiel"

    override fun getItem(identifier: String): ItemStack? {
        return ZaphkielAPI.getItem(identifier)?.itemStack
    }

}