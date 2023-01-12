package cc.trixey.invero.bukkit.api

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.common.Panel

/**
 * Invero
 * cc.trixey.invero.bukkit.api.InveroManager
 *
 * @author Arasple
 * @since 2023/1/5 21:16
 */
class InveroManager : Manager<BukkitWindow> {

    override val registeredWindows = mutableSetOf<BukkitWindow>()

    override fun unregister(window: BukkitWindow) {
        registeredWindows -= window
    }

    override fun register(window: BukkitWindow) {
        registeredWindows += window
    }

    override fun findWindow(panel: Panel): BukkitWindow? {
        return registeredWindows.find { panel in it }
    }

}