package cc.trixey.invero.core.impl

import cc.trixey.invero.bukkit.util.toBukkitViewer
import cc.trixey.invero.common.Viewer

/**
 * Invero
 * cc.trixey.invero.core.impl.Util
 *
 * @author Arasple
 * @since 2023/1/14 17:15
 */
fun Viewer.getMenuSession(): DefaultMenuSession {
    return DefaultMenuSession.get(toBukkitViewer())
}