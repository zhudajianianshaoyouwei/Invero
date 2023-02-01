package cc.trixey.invero.core

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.Menu
import cc.trixey.invero.core.util.session
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Baffle
import taboolib.module.nms.MinecraftVersion.majorLegacy
import taboolib.platform.util.isOffhand
import java.util.concurrent.TimeUnit

/**
 * Invero
 * cc.trixey.invero.core.Listener
 *
 * @author Arasple
 * @since 2023/1/25 11:42
 */
object Listener {

    private val baffle by lazy {
        Baffle.of(1_000, TimeUnit.MILLISECONDS)
    }

    @SubscribeEvent
    fun bindingsItem(e: PlayerInteractEvent) {
        if (majorLegacy >= 10900 && e.isOffhand()) return
        if (e.player.session != null) return
        if (!baffle.hasNext(e.player.name)) return
        val item = e.item ?: return

        Invero.api()
            .getMenuManager()
            .findBound(item)
            ?.let {
                e.isCancelled = true
                it.open(e.player)
            }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun bindingsChatCommand(e: PlayerCommandPreprocessEvent) {
        if (!baffle.hasNext(e.player.name)) return

        val message = e.message

        if (message.length > 1)
            runChatBindings(message)
                ?.let {
                    it.open(e.player)
                    e.isCancelled = true
                }

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun bindingsChat(e: AsyncPlayerChatEvent) {
        if (!baffle.hasNext(e.player.name)) return

        runChatBindings(e.message)
            ?.let {
                it.open(e.player)
                e.isCancelled = true
            }
    }

    private fun runChatBindings(message: String): Menu? {
        return Invero.api().getMenuManager().findBound(message)
    }

}