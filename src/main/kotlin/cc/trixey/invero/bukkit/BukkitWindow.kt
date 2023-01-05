package cc.trixey.invero.bukkit

import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.WindowType
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/12/29 12:54
 */
abstract class BukkitWindow(type: WindowType, title: String) : BaseWindow(type, title) {

    abstract override val inventory: BukkitInventory

    fun open(player: Player) = open(BukkitViewer(player))

    override fun render() {
        panels
            .sortedBy { it.weight }
            .forEach {
                it.render()
            }
    }

    override fun open(viewer: Viewer) {
        if (viewers.add(viewer)) {
            inventory.open(viewer)
            render()
        } else {
            error("Viewer {$viewer} is already viewing this window")
        }
    }

    override fun close(viewer: Viewer) {
        if (viewers.remove(viewer))
            inventory.close(viewer)
    }

}