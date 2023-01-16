package cc.trixey.invero.core

import cc.trixey.invero.bukkit.util.CoroutineTask
import taboolib.common.platform.service.PlatformExecutor

/**
 * Invero
 * cc.trixey.invero.core.TaskManager
 *
 * @author Arasple
 * @since 2023/1/16 12:14
 */
class TaskManager {

    val platformTasks: MutableSet<PlatformExecutor.PlatformTask> = mutableSetOf()

    val coroutineTasks: MutableSet<CoroutineTask> = mutableSetOf()

    fun unregisterAll() {
        platformTasks.removeIf {
            it.cancel()
            true
        }
        coroutineTasks.removeIf {
            it.cancel()
            true
        }
    }

    operator fun plusAssign(task: PlatformExecutor.PlatformTask) {
        platformTasks += task
    }

    operator fun plusAssign(coroutineTask: CoroutineTask) {
        coroutineTasks += coroutineTask
    }

    override fun toString(): String {
        return "MenuTasks(${platformTasks.size}, ${coroutineTasks.size})"
    }

}