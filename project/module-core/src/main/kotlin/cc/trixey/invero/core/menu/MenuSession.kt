package cc.trixey.invero.core.menu

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuSession
 *
 * @author Arasple
 * @since 2023/1/14 16:50
 */
interface MenuSession {

    /**
     * 玩家
     */
    val viewer: BukkitViewer

    /**
     * 会话上下文
     */
    val context: MenuContext

    /**
     * 菜单
     */
    val menu: Menu?

    /**
     * 正在查看的 Window
     */
    val viewingWindow: Window?

}