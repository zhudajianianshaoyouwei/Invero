package cc.trixey.invero.core.item.source

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.platform.util.sendLang

/**
 * Invero
 * cc.trixey.invero.core.item.source.Provider
 *
 * @author Arasple
 * @since 2023/1/29 15:36
 */
abstract class Provider {

    open val pluginName: String? = null

    val isHooked by lazy {
        if (pluginName != null) {
            val plugin = Bukkit.getPluginManager().getPlugin(pluginName!!)
            (plugin != null && plugin.isEnabled).also {
                if (it) {
                    console().cast<CommandSender>().sendLang("plugin-hooked", pluginName!!)
                }
            }
        } else {
            true
        }
    }

    abstract fun getItem(identifier: String): ItemStack?

}