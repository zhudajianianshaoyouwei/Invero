package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.api.dsl.bukkitChestWindow
import cc.trixey.invero.bukkit.api.dsl.packetChestWindow
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.core.util.debug
import cc.trixey.invero.core.util.getSession
import cc.trixey.invero.serialize.ListScoping
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.core.Menu
 *
 * @author Arasple
 * @since 2023/1/15 17:16
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class Menu(
    @Transient
    var name: String? = null,
    @SerialName("menu")
    @JsonNames("menu", "settings", "init")
    val settings: MenuSettings,
    @Serializable(with = ListScoping::class)
    val panels: List<AgentPanel>
) {

    fun open(player: Player) {
        return open(BukkitViewer(player))
    }

    fun open(viewer: Viewer) {
        val session = viewer.getSession()
        val window = if (requireBukkitWindow()) {
            bukkitChestWindow(settings.containerType.rows, settings.title.getDefault(), settings.options.storageMode)
        } else {
            packetChestWindow(settings.containerType.rows, settings.title.getDefault(), settings.options.storageMode)
        }

        window.onClose { _, it -> this@Menu.close(it) }

        session.menu = this
        session.viewingWindow = window
        settings.title.invoke(session)

        panels.forEach { it.invoke(session) }

        window.open(viewer)
    }

    fun close(viewer: Viewer) {
        val session = viewer.getSession()

        session.taskClosure()
        session.menu = this
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