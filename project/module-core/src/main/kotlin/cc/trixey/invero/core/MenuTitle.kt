package cc.trixey.invero.core

import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.toCyclic
import cc.trixey.invero.core.serialize.ListStringSerializer
import cc.trixey.invero.core.util.containsAnyPlaceholder
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
    @Serializable(with = ListStringSerializer::class)
    val value: List<String>,
    val period: Long?,
    val mode: CycleMode?
) {

    fun invoke(session: Session) {
        if (isSingle() && !value.single().containsAnyPlaceholder()) {
            return
        }

        val menuId = session.menu.name
        val cyclic = value.toCyclic(mode ?: CycleMode.LOOP)
        session.taskMgr.launchAsync(delay = period!!, period = period) {
            if (session.variables["title_task_running"] != false) {
                session.window.title = cyclic.getAndCycle().let { session.parse(it) }
            }
        }
    }

    fun isSingle() = value.size <= 1 || period == null || mode == null || period < 0

    fun getDefault() = value.getOrElse(0) { "Untitled" }

}
