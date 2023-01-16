package cc.trixey.invero.core.session

import taboolib.common.platform.service.PlatformExecutor

/**
 * Invero
 * cc.trixey.invero.core.session.MenuTask
 *
 * @author Arasple
 * @since 2023/1/16 12:14
 */
@JvmInline
value class MenuTask(val tasks: MutableSet<PlatformExecutor.PlatformTask>) {

    operator fun plusAssign(task: PlatformExecutor.PlatformTask) {
        tasks += task
    }

}