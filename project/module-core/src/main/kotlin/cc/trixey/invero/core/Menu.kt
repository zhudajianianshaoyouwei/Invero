@file:OptIn(ExperimentalSerializationApi::class)

package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.api.dsl.chestWindow
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.core.serialize.ListAgentPanelSerializer
import cc.trixey.invero.core.util.debug
import cc.trixey.invero.core.util.getSession
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.core.Menu
 *
 * @author Arasple
 * @since 2023/1/15 17:16
 */
@Serializable
class Menu(
    @Transient
    var name: String? = null,
    @SerialName("menu")
    val settings: MenuSettings,
    @Serializable(with = ListAgentPanelSerializer::class)
    @JsonNames("panel", "pane", "panes")
    val panels: List<AgentPanel>
) {

    fun open(player: Player) {
        return open(BukkitViewer(player))
    }

    fun close(player: Player) {
        return close(BukkitViewer(player))
    }

    fun open(viewer: Viewer) {
        val session = viewer.getSession()

        if (session.menu != null) session.newMenuEnv()

        submitAsync {
            val window = chestWindow(
                !requireBukkitWindow(),
                settings.containerType.rows,
                settings.title.getDefault(),
                settings.options.storageMode
            )

            window.onClose { _, it -> this@Menu.close(it) }
            session.menu = this@Menu
            session.viewingWindow = window
            panels.forEach { it.invoke(window, session) }
            window.open(viewer)
            settings.title.invoke(session)
        }
    }

    fun close(viewer: Viewer) {
        val session = viewer.getSession()

        session.taskClosure()
        session.menu = null
        session.viewingWindow?.close(viewer)
        session.viewingWindow = null

        debug(
            """
                Menu closed.
                Session ${session.taskManager}
            """.trimIndent()
        )
    }

    private fun requireBukkitWindow(): Boolean {
        return panels.any { it.requireBukkitWindow() }
    }

}