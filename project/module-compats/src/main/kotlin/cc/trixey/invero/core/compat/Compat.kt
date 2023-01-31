package cc.trixey.invero.core.compat

import cc.trixey.invero.core.compat.item.HDBItemProvider
import cc.trixey.invero.core.compat.item.ItemsAdderItemProvider
import cc.trixey.invero.core.compat.item.OraxenItemProvider
import cc.trixey.invero.core.compat.item.ZaphkielItemProvider
import cc.trixey.invero.core.item.source.Provider
import cc.trixey.invero.core.item.source.SourceProviderManager
import taboolib.common.platform.Awake

/**
 * Invero
 * cc.trixey.invero.core.compat.Compat
 *
 * @author Arasple
 * @since 2023/1/29 15:43
 */
object Compat {

    @Awake
    fun inject() {
        registerItemSources()
    }

    internal fun registerItemSources() {
        ZaphkielItemProvider().register("zaphkiel", "zap")
        OraxenItemProvider().register("oraxen")
        ItemsAdderItemProvider().register("itemsadder", "ia")
        HDBItemProvider().register("headdatabase", "hdb")
    }

    private fun Provider.register(vararg namespace: String) {
        if (this is PluginHook && !isHooked) return
        namespace.forEach { SourceProviderManager.register(it, this) }
    }

}