package cc.trixey.invero.core.compat.item

import cc.trixey.invero.common.supplier.ItemSourceProvider
import cc.trixey.invero.core.compat.PluginHook
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.compat.item.ZaphkielItemProvider
 *
 * @author Arasple
 * @since 2023/1/29 15:44
 */
class ZaphkielItemProvider : ItemSourceProvider, PluginHook() {

    override val pluginName = "Zaphkiel"

    override fun getItem(identifier: String): ItemStack? {
        return ZaphkielAPI.getItem(identifier)?.itemStack
    }

}