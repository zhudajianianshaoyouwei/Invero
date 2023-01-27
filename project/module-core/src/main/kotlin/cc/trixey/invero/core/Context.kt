package cc.trixey.invero.core

import cc.trixey.invero.bukkit.PlayerViewer
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Window
import cc.trixey.invero.common.panel.PagedPanel
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
    val extenedVars: Map<String, Any>? = null
) {

    val player: Player
        get() = viewer.get()

    val menu: Menu?
        get() = session?.menu

    val window: Window?
        get() = session?.window

    val variables: Map<String, Any>
        get() = (session?.getVariables() ?: emptyMap()) + buildMap {
            if (panel != null) put("@panel", panel)
            if (icon != null) put("@icon", icon)
            if (menu != null) put("@menu", menu!!)

            if (panel is PagedPanel) {
                put("page", panel.pageIndex)
                put("page_max", panel.maxPageIndex)
            }
        } + (extenedVars ?: emptyMap())

}