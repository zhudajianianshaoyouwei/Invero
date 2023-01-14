package cc.trixey.invero.core.impl.panel

import cc.trixey.invero.bukkit.api.dsl.fillup
import cc.trixey.invero.bukkit.api.dsl.firstAvailablePositionForPanel
import cc.trixey.invero.bukkit.api.dsl.item
import cc.trixey.invero.bukkit.api.dsl.standard
import cc.trixey.invero.bukkit.panel.StandardPanel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.icon.Icon
import cc.trixey.invero.core.menu.Layout
import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.menu.PanelAgent
import org.bukkit.Material

/**
 * Invero
 * cc.trixey.invero.core.impl.panel.NormalPanel
 *
 * @author Arasple
 * @since 2023/1/14 16:59
 */
class PanelSimple(
    override val scale: Scale,
    override val layout: Layout,
    override val locate: Pos? = null,
    override val icons: List<Icon> = listOf()
) : PanelAgent<StandardPanel> {

    override fun apply(session: MenuSession) {
        val window = session.viewingWindow ?: error("no window apply NIMA")

        window.apply {
            standard(scale.raw, locate?.value ?: firstAvailablePositionForPanel()) {
                item(Material.STONE).fillup()
            }
        }
    }

}