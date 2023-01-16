package cc.trixey.invero.core.util

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.Session

/**
 * Invero
 * cc.trixey.invero.core.util.Viewer
 *
 * @author Arasple
 * @since 2023/1/16 12:12
 */
fun Viewer.getSession(): Session {
    return Session.get(this as BukkitViewer)
}