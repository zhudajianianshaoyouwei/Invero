package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.PacketWindow
import cc.trixey.invero.common.Window
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.parseMiniMessage
import cc.trixey.invero.core.util.translateAmpersandColor
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.platform.compat.replacePlaceholder
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.Session
 *
 * @author Arasple
 * @since 2023/1/15 22:40
 */
class Session(val viewer: BukkitViewer) {

    var menu: Menu? = null

    var viewingWindow: Window? = null

    val taskManager: TaskManager = TaskManager()

    var taskTitleFrame: Boolean = true

    val variables: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun generateVariables(): Map<String, String> {
        return variables
    }

    fun closeMenu() {
        menu?.close(viewer.get())
    }

    fun newMenuEnv() {
        // 注销任务
        taskClosure()
        // 安全关闭 Invero.Window
        viewingWindow?.let {
            if (it is PacketWindow) it.close(viewer, false)
            else it.close(viewer)
        }
        viewingWindow = null
        menu = null
    }

    fun taskClosure() {
        taskManager.unregisterAll()
    }

    fun parse(input: String): String {
        val player = viewer.get()

        return if (input.isBlank()) input
        else KetherHandler
            .parseInline(input, player, generateVariables())
            .parseMiniMessage()
            .translateAmpersandColor()
            .replacePlaceholder(player)
    }

    fun parse(input: List<String>): List<String> {
        return input.map { parse(it) }
    }

    fun launch(
        now: Boolean = false,
        async: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: (task: PlatformExecutor.PlatformTask) -> Unit,
    ) {
        submit(now, async, delay, period, comment, executor).also { taskManager += it }
    }

    fun launchAsync(
        now: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: (task: PlatformExecutor.PlatformTask) -> Unit,
    ) = launch(now, true, delay, period, comment, executor)

    companion object {

        private val sessions = ConcurrentHashMap<UUID, Session>()

        fun get(viewer: BukkitViewer): Session {
            return sessions.computeIfAbsent(viewer.uuid) { Session(viewer) }
        }

    }

}