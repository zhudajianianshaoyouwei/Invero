package cc.trixey.invero.core.task

import taboolib.common.platform.service.PlatformExecutor

/**
 * Invero
 * cc.trixey.invero.core.task.MenuTask
 *
 * @author Arasple
 * @since 2023/1/14 17:57
 */
@JvmInline
value class MenuTask(val tasks: MutableSet<PlatformExecutor.PlatformTask>) {

    operator fun plusAssign(task: PlatformExecutor.PlatformTask) {
        tasks += task
    }

}