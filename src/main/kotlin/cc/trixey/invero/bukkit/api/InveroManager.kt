package cc.trixey.invero.bukkit.api

import cc.trixey.invero.api.Manager
import cc.trixey.invero.bukkit.BaseWindow
import cc.trixey.invero.common.Panel

/**
 * @author Arasple
 * @since 2023/1/5 21:16
 */
class InveroManager : Manager<BaseWindow> {

    override val registeredWindows = mutableSetOf<BaseWindow>()

    override fun unregister(window: BaseWindow) {
        registeredWindows -= window
    }

    override fun register(window: BaseWindow) {
        registeredWindows += window
    }

    override fun findWindow(panel: Panel): BaseWindow? {
        return registeredWindows.find { panel in it }
    }

}