package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.bukkit.util.CoroutineTask
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.parseMiniMessage
import cc.trixey.invero.core.util.translateAmpersandColor
import org.bukkit.entity.Player
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
class Session(val viewer: PlayerViewer) {

    var menu: Menu? = null

    var window: BukkitWindow? = null

    val variables: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    val taskManager = ConcurrentHashMap<UUID, TaskManager>()

    fun closeMenu() {
        menu?.close(viewer)
    }

    fun getTaskManager(window: BukkitWindow? = null): TaskManager {
        return taskManager.computeIfAbsent(window?.uniqueId ?: this.window!!.uniqueId) { TaskManager() }
    }

    fun unregisterTasks(window: BukkitWindow) {
        getTaskManager(window).unregisterAll()
        taskManager.remove(window.uniqueId)
    }

    fun unregisterAll() {
        taskManager.values.forEach { it.unregisterAll() }
        taskManager.clear()
    }

    fun registerTask(task: CoroutineTask) {
        getTaskManager() += task
    }

    fun parse(input: String, context: Context? = null): String {
        val player = viewer.get<Player>()

        return if (input.isBlank()) input
        else KetherHandler
            .parseInline(input, player, context?.variables ?: variables)
            .parseMiniMessage()
            .translateAmpersandColor()
            .replacePlaceholder(player)
    }

    fun parse(input: List<String>, context: Context? = null): List<String> {
        return input.map { parse(it, context) }
    }

    fun launch(
        now: Boolean = false,
        async: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: (task: PlatformExecutor.PlatformTask) -> Unit,
    ) {
        submit(now, async, delay, period, comment, executor).also { getTaskManager() += it }
    }

    fun launchAsync(
        now: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: (task: PlatformExecutor.PlatformTask) -> Unit,
    ) = launch(now, true, delay, period, comment, executor)

    companion object {

        private val sessions = ConcurrentHashMap<String, Session>()

        fun get(viewer: PlayerViewer): Session {
            return sessions.computeIfAbsent(viewer.name) { Session(viewer) }
        }

    }

}