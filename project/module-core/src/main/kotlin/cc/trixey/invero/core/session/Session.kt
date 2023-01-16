package cc.trixey.invero.core.session

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.core.Menu
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.session.Session
 *
 * @author Arasple
 * @since 2023/1/15 22:40
 */
class Session(
    val viewer: BukkitViewer,
    val context: MenuContext = MenuContext(),
    var menu: Menu? = null,
    var viewingWindow: Window? = null
) {

    private val menuTasks = mutableMapOf<String, MenuTask>()

    fun getMenuTask(): MenuTask {
        return menuTasks[menu!!.name]!!
    }

    fun taskClosure() {
        getMenuTask().tasks.removeIf {
            it.cancel()
            true
        }
    }

    fun parse(input: String): String {
        return input
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
        submit(now, async, delay, period, comment, executor).also { getMenuTask() += it }
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