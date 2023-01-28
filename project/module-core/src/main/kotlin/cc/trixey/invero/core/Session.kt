package cc.trixey.invero.core

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.core.Session.Companion.VarType.*
import cc.trixey.invero.core.compat.parseMiniMessage
import cc.trixey.invero.core.compat.translateAmpersandColor
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.core.util.session
import org.bukkit.entity.Player
import taboolib.common.platform.function.submitAsync
import taboolib.expansion.getDataContainer
import taboolib.platform.compat.replacePlaceholder
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.Session
 *
 * @author Arasple
 * @since 2023/1/15 22:40
 */
class Session(
    val viewer: PlayerViewer,
    val menu: Menu,
    val window: BukkitWindow,
    variables: Map<String, Any> = emptyMap()
) {

    val createdTime: Long = System.currentTimeMillis()

    private val variables: ConcurrentHashMap<String, Any> = ConcurrentHashMap(variables)

    val taskMgr: TaskManager
        get() = TaskManager.get(viewer.name)

    init {
        updateVariables()
    }

    fun hasVariable(key: String): Boolean {
        return variables.containsKey(key)
    }

    fun updateVariables() {
        variables += InveroDatabase.globalDataDatabase.source
        variables += viewer.get<Player>().getDataContainer().source
    }

    fun getVariables(): ConcurrentHashMap<String, Any> {
        return variables
    }

    fun getVariable(key: String): Any? {
        return variables[key]
    }

    fun setVariable(key: String, value: Any) {
        // push to database
        when (key.varType) {
            GLOBAL -> InveroDatabase.globalDataDatabase[key] = value
            PLAYER -> viewer.get<Player>().getDataContainer()[key] = value
            TEMP -> {}
        }
        variables[key] = value
    }

    fun removeVariable(key: String) {
        // push to database
        when (key.varType) {
            GLOBAL -> InveroDatabase.globalDataDatabase.source.remove(key)
            PLAYER -> viewer.get<Player>().getDataContainer().source.remove(key)
            TEMP -> {}
        }
        variables.remove(key)
    }

    fun elapsed(): Long {
        return System.currentTimeMillis() - createdTime
    }

    fun parse(input: String, context: Context? = null): String {
        val player = viewer.get<Player>()

        return if (input.isBlank()) input
        else KetherHandler
            .parseInline(input, player, context?.variables ?: variables)
            .replacePlaceholder(player)
            .translateAmpersandColor()
            .parseMiniMessage()
    }

    fun parse(input: List<String>, context: Context? = null): List<String> {
        return input.map { parse(it, context) }
    }

    companion object {

        private val sessions = ConcurrentHashMap<String, Session>()

        fun getSession(viewer: PlayerViewer): Session? {
            return sessions[viewer.name]
        }

        fun register(viewer: PlayerViewer, menu: Menu, window: BukkitWindow, variables: Map<String, Any>): Session {
            val session = Session(viewer, menu, window, variables)
            sessions[viewer.name] = session
            return session
        }

        fun unregister(session: Session) {
            sessions.remove(session.viewer.name, session)
            session.taskMgr.unregisterAll()
            val viewer = session.viewer
            submitAsync(delay = 40L) { if (viewer.session == null) TaskManager.get(viewer.name).unregisterAll(true) }
        }

        val String.varType: VarType
            get() = values().find { startsWith(it.prefix) } ?: TEMP

        enum class VarType {

            TEMP,

            GLOBAL,

            PLAYER;

            val prefix = name.lowercase() + "@"

        }

    }

}