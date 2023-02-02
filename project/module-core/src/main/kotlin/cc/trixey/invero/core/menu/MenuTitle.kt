@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core.menu

import cc.trixey.invero.core.Session
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.toCyclic
import cc.trixey.invero.core.serialize.ListStringSerializer
import cc.trixey.invero.core.util.containsAnyPlaceholder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuTitle
 *
 * @author Arasple
 * @since 2023/1/25 11:36
 */
@Serializable
class MenuTitle(
    @Serializable(with = ListStringSerializer::class)
    @JsonNames("values")
    val value: List<String>,
    val period: Long?,
    @JsonNames("type")
    val mode: CycleMode?
) {

    @Transient
    val isSingle = value.size <= 1 || period == null || mode == null || period < 0

    @Transient
    val default = value.getOrElse(0) { "Untitled" }

    fun invoke(session: Session) {
        if (isSingle && !value.single().containsAnyPlaceholder() && period == null) {
            return
        }
        val cyclic = value.toCyclic(mode ?: CycleMode.LOOP)
        if (period != null) {
            session.taskGroup.launchAsync(delay = period, period = period) {
                if (session.getVariable("title_task_running") != false) {
                    session.window.title = cyclic.getAndCycle().let { session.parse(it) }
                }
            }
        }
    }

}