package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.common.*
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * @author Arasple
 * @since 2022/12/22 20:50
 */
abstract class BukkitPanel(
    override val parent: PanelContainer,
    override val weight: PanelWeight,
    override val scale: Scale,
    override val locate: Pos
) : Panel {

    private var handler: (WindowClickEvent, Clickable) -> Unit = { _, _ -> }

    override fun getHandler(): (WindowClickEvent, Clickable) -> Unit {
        return handler
    }

    override fun setHandler(handler: (WindowClickEvent, Clickable) -> Unit) {
        this.handler = handler
    }

    override val area: Set<Pos> by lazy {
        scale.getArea(locate)
    }

    override val window: Window by lazy {

        InveroAPI.manager.findWindow(this) ?: let {
            var p = parent
            while (p.isPanel()) {
                p = p.cast<Panel>().parent
            }

            p as Window
        }

    }

}