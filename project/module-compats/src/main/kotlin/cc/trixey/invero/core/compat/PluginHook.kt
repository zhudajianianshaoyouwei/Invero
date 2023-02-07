package cc.trixey.invero.core.compat

import org.bukkit.Bukkit
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Invero
 * cc.trixey.invero.core.compat.PluginHook
 *
 * @author Arasple
 * @since 2023/1/31 20:39
 */
abstract class PluginHook {

   open val pluginName: String? = null

    val isHooked: Boolean by lazy {
        if (pluginName != null) {
            val plugin = Bukkit.getPluginManager().getPlugin(pluginName!!)
            (plugin != null && plugin.isEnabled).also {
                if (it) {
                    console().sendLang("plugin-hooked", pluginName!!)
                }
            }
        } else false
    }

}