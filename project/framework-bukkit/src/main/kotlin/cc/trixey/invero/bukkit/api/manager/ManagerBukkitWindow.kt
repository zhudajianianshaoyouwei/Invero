package cc.trixey.invero.bukkit.api.manager

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.api.Manager

/**
 * Invero
 * cc.trixey.invero.bukkit.api.manager.ManagerBukkitWindow
 *
 * @author Arasple
 * @since 2023/1/5 21:16
 */
class ManagerBukkitWindow : Manager<BukkitWindow> {

    override val registeredWindows = mutableSetOf<BukkitWindow>()

}