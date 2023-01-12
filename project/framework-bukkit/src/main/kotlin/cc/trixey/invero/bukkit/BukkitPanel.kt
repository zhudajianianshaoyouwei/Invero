package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.bukkit.element.Clickable
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.event.WindowDragEvent
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.BukkitPanel
 *
 * @author Arasple
 * @since 2022/12/22 20:50
 */
abstract class BukkitPanel(
    override val parent: PanelContainer,
    override val weight: PanelWeight,
    override val scale: Scale,
    override val locate: Pos
) : Panel, Clickable<BukkitPanel> {

    private val handlers = mutableSetOf<(WindowClickEvent, BukkitPanel) -> Any>()

    override fun addHandler(handler: (WindowClickEvent, BukkitPanel) -> Any) {
        handlers += handler
    }

    override fun runHandler(event: WindowClickEvent): Boolean {
        return handlers.none { it(event, getInstance()) == false }
    }

    override val area by lazy { scale.getArea(locate) }

    override val window by lazy { InveroAPI.manager.findWindow(this) ?: parent.getTopWindow() }

    override fun getInstance(): BukkitPanel {
        return this
    }

    override fun handleDrag(positions: List<Pos>, e: WindowDragEvent): Boolean {
        return true
    }

}