package cc.trixey.invero.core.menu

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import taboolib.common.platform.service.PlatformExecutor

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

    /**
     * 替换变量
     */
    fun parse(input: String): String

    fun parse(input: List<String>): List<String> = input.map { parse(it) }

    fun launch(
        now: Boolean = false,
        async: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: (task: PlatformExecutor.PlatformTask) -> Unit,
    )

    fun launchAsync(
        now: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: (task: PlatformExecutor.PlatformTask) -> Unit,
    ) = launch(now, true, delay, period, comment, executor)

}