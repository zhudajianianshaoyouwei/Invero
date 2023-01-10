package cc.trixey.invero.bukkit.api

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Window

/**
 * Invero
 * cc.trixey.invero.bukkit.api.Manager
 *
 * @author Arasple
 * @since 2023/1/5 21:15
 */
interface Manager<T : Window> {

    val registeredWindows: MutableSet<T>

    fun register(window: T)

    fun unregister(window: T)

    fun findWindow(panel: Panel): T?

}