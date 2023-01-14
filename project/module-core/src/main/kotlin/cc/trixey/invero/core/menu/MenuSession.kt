package cc.trixey.invero.core.menu

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuSession
 *
 * @author Arasple
 * @since 2023/1/14 14:13
 */
class MenuSession(
    val viewer: BukkitViewer,
    val context: MenuContext,
    var menu: Menu? = null,
    var viewingWindow: Window? = null
) {

}