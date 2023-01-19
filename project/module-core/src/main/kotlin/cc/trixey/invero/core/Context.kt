package cc.trixey.invero.core

import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Window
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
    val session: Session,
    val panel: Panel? = null,
    val icon: IconElement? = null,
) {

    val player: Player
        get() = session.viewer.get()

    val menu: Menu?
        get() = session.menu

    val window: Window?
        get() = session.window

    val variables: Map<String, Any>
        get() = session.variables + buildMap {
            if (panel != null) put("@panel", panel)
            if (icon != null) put("@icon", icon)
            if (menu != null) put("@menu", menu!!)
        }

}