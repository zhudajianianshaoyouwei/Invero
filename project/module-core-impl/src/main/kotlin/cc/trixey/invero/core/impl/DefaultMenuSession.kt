package cc.trixey.invero.core.impl

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.task.MenuTask
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.impl.DefaultMenuSession
 *
 * @author Arasple
 * @since 2023/1/14 17:13
 */
class DefaultMenuSession(
    override val viewer: BukkitViewer,
    override val context: DefaultMenuContext = DefaultMenuContext(),
    override var menu: DefaultMenu? = null,
    override var viewingWindow: Window? = null
) : MenuSession {

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

    fun launch(
        now: Boolean = false,
        async: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: PlatformExecutor.PlatformTask.() -> Unit,
    ) = submit(now, async, delay, period, comment, executor).also { getMenuTask() += it }

    fun launchAsync(
        now: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        comment: String? = null,
        executor: PlatformExecutor.PlatformTask.() -> Unit,
    ) = launch(now, true, delay, period, comment, executor)

    companion object {

        private val sessions = ConcurrentHashMap<UUID, DefaultMenuSession>()

        fun get(viewer: BukkitViewer): DefaultMenuSession {
            return sessions.computeIfAbsent(viewer.uuid) { DefaultMenuSession(viewer) }
        }

    }

}