package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.util.CoroutineTask
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

    var window: Window? = null

    val variables: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    private val taskManager: TaskManager = TaskManager()

    fun closeMenu() {
        menu?.close(viewer.get())
    }

    /**
     * 准备切换菜单
     */
    fun prepareTransfer() {
        // 注销任务
        unregisterTasks()
        // 伪关闭 Invero.Window，防止鼠标指针跑偏
        window?.close(viewer, closeInventory = false, updateInventory = false)

        window = null
        menu = null
    }

    /**
     * 注销所有菜单任务
     */
    fun unregisterTasks() {
        taskManager.unregisterAll()
    }

    /**
     * 注册任务
     */
    fun registerTask(task: CoroutineTask) {
        taskManager += task
    }

    fun parse(input: String): String {
        val player = viewer.get()

        return if (input.isBlank()) input
        else KetherHandler
            .parseInline(input, player, variables)
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