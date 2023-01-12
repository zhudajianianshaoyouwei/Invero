package cc.trixey.invero.bukkit.api

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.api.manager.ManagerBukkitWindow
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Window

/**
 * Invero
 * cc.trixey.invero.bukkit.api.InveroAPI
 *
 * @author Arasple
 * @since 2023/1/5 13:30
 */
object InveroAPI {

    val bukkitManager = ManagerBukkitWindow()

    fun findWindow(panel: Panel): Window? {
        return bukkitManager.findWindow(panel)
    }

    fun findWindow(viewer: BukkitViewer): Window? {
        return bukkitManager.registeredWindows.find { viewer in it.viewers }
    }

}