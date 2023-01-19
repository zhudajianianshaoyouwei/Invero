package cc.trixey.invero.core

import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.toCyclic
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.MenuTitle
 *
 * @author Arasple
 * @since 2023/1/15 19:53
 */
@Serializable
class MenuTitle(
    val value: List<String>,
    val period: Long?,
    val mode: CycleMode?
) {

    fun invoke(session: Session) {
        if (isSingle()) return

        val cyclic = value.toCyclic(mode ?: CycleMode.LOOP)
        session.launchAsync(delay = period!!, period = period) {
            if (session.taskTitleFrame) {
                session.viewingWindow?.title = cyclic.getAndCycle()
            }
        }
    }

    fun isSingle() = value.size <= 1 || period == null || mode == null || period < 0

    fun getDefault() = value.getOrElse(0) { "Untitled" }

}
