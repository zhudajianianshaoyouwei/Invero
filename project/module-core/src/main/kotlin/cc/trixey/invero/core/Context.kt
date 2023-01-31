package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.icon.IconElement
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.core.Context
 *
 * @author Arasple
 * @since 2023/1/17 11:14
 */
class Context(
    val viewer: PlayerViewer,
    val session: Session? = null,
    val panel: Panel? = null,
    val icon: IconElement? = null,
    val vars: Map<String, Any> = emptyMap()
) {

    fun parse(input: String): String {
        return session?.parse(input, this) ?: "CONTEXT HAS NO VALID SESSION"
    }

    fun parse(input: List<String>): List<String> {
        return input.map { parse(it) }
    }

    val player: Player
        get() = viewer.get()

    val menu: Menu?
        get() = session?.menu

    private val contextVariables: Map<String, Any> = buildMap {
        icon?.let { put("@icon", it) }
        menu?.let { put("@menu", it) }
        panel?.let { put("@panel", it) }
        put("@context", this@Context)
        this += vars
    }

    val variables: Map<String, Any>
        get() = session?.getVariables(contextVariables) ?: emptyMap()

}