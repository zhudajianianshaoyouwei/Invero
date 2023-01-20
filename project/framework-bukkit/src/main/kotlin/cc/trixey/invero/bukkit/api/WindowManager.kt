package cc.trixey.invero.bukkit.api

import cc.trixey.invero.bukkit.BukkitWindow
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.bukkit.api.WindowManager
 *
 * @author Arasple
 * @since 2023/1/20 13:08
 */
val registeredWindows = ConcurrentHashMap<String, BukkitWindow>()

fun findWindow(viewer: String): BukkitWindow? {
    return registeredWindows[viewer]
}

fun BukkitWindow.register() {
    require(!registeredWindows.containsKey(viewer.name))

    registeredWindows[viewer.name] = this
}

fun BukkitWindow.unregister() {
    registeredWindows.remove(viewer.name, this)
}