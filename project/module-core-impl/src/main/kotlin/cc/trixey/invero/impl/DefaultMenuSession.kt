package cc.trixey.invero.impl

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Window
import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.task.MenuTask
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.platform.compat.replacePlaceholder
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.impl.DefaultMenuSession
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

    override fun parse(input: String): String {
        // kether inline
        // {{player name}}
        // {0/1/2/3} args
        // %xxx_placeholder% placeholder
        context.getArguments()
        context.getMenuRoute()
        // TODO
        return input.replacePlaceholder(viewer.get())
    }

    fun getMenuTask(): MenuTask {
        return menuTasks[menu!!.name]!!
    }

    fun taskClosure() {
        getMenuTask().tasks.removeIf {
            it.cancel()
            true
        }
    }

    override fun launch(
        now: Boolean,
        async: Boolean,
        delay: Long,
        period: Long,
        comment: String?,
        executor: PlatformExecutor.PlatformTask.() -> Unit,
    ) {
        submit(now, async, delay, period, comment, executor).also { getMenuTask() += it }
    }

    companion object {

        private val sessions = ConcurrentHashMap<UUID, DefaultMenuSession>()

        fun get(viewer: BukkitViewer): DefaultMenuSession {
            return sessions.computeIfAbsent(viewer.uuid) { DefaultMenuSession(viewer) }
        }

    }

}