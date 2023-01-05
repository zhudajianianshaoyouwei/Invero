package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.InveroAPI
import cc.trixey.invero.common.*
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Arasple
 * @since 2022/12/22 20:50
 */
abstract class BukkitPanel(
    override val parent: PanelContainer,
    override val weight: PanelWeight,
    override val scale: IScale,
    override val locate: Pos
) : Panel {

    override val window: Window
        get() {
            return InveroAPI.manager.findWindow(this) ?: let {
                var p = parent
                while (p.isPanel()) {
                    p = p.cast<Panel>().parent
                }

                p as Window
            }
        }

    abstract fun handleClick(pos: Pos, e: InventoryClickEvent)

}