package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.parseMiniMessage
import cc.trixey.invero.core.util.session
import cc.trixey.invero.core.util.translateAmpersandColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.Session
 *
 * @author Arasple
 * @since 2023/1/15 22:40
 */
class Session(val viewer: PlayerViewer, val menu: Menu, val window: BukkitWindow) {

    val createdTime: Long = System.currentTimeMillis()

    val variables = ConcurrentHashMap<String, Any>()

    val taskMgr: TaskManager
        get() = TaskManager.get(viewer.name)

    fun elapsed(): Long {
        return System.currentTimeMillis() - createdTime
    }

    fun parse(input: String, context: Context? = null): String {
        val player = viewer.get<Player>()

        return if (input.isBlank()) input
        else KetherHandler
            .parseInline(input, player, context?.variables ?: variables)
            .parseMiniMessage()
            .translateAmpersandColor()
            .replacePlaceholder(player)
    }

    fun parse(input: List<String>, context: Context? = null): List<String> {
        return input.map { parse(it, context) }
    }

    companion object {

        private val sessions = ConcurrentHashMap<String, Session>()

        fun getSession(viewer: PlayerViewer): Session? {
            return sessions[viewer.name]
        }

        fun register(viewer: PlayerViewer, menu: Menu, window: BukkitWindow): Session {
            val session = Session(viewer, menu, window)
            sessions[viewer.name] = session
            return session
        }

        fun unregister(session: Session) {
            sessions.remove(session.viewer.name, session)
            session.taskMgr.unregisterAll()
            val viewer = session.viewer
            // patch for unwanted issues
            submitAsync(delay = 40L) { if (viewer.session == null) TaskManager.get(viewer.name).unregisterAll(true) }
        }

        @Awake(LifeCycle.DISABLE)
        fun unregister() {
            Bukkit.getScheduler().apply {
                pendingTasks
                    .filter { it.owner == bukkitPlugin && !it.isCancelled }
                    .forEach { it.cancel() }
            }

            sessions.values.forEach { unregister(it) }
        }

    }

}