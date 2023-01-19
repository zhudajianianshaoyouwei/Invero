package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.event.PacketWindowOpenEvent
import cc.trixey.invero.bukkit.nms.updateTitle
import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.common.event.WindowClickEvent
import cc.trixey.invero.common.event.WindowDragEvent
import cc.trixey.invero.common.event.WindowItemsCollectEvent
import cc.trixey.invero.common.event.WindowItemsMoveEvent
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.bukkit.PacketWindow
 *
 * @author Arasple
 * @since 2023/1/12 15:43
 */
abstract class PacketWindow(
    type: ContainerType,
    storageMode: StorageMode,
    title: String = "Untitled_Invero_Window"
) : BukkitWindow(type, storageMode, title) {

    override var title = title
        set(value) {
            field = value
            submit {
                updateTitle(value)
                forViewers<BukkitViewer> { inventory.updateWindowItems(it) }
            }
        }

    companion object {


    }

    abstract override val inventory: PacketInventory

    override fun open(viewer: Viewer) {
        val event = PacketWindowOpenEvent(viewer as BukkitViewer, this).also { it.call() }
        submit { if (!event.isCancelled) super.open(viewer) }
    }

    override fun handleClick(e: WindowClickEvent) {
        super.handleClick(e)

        if (e.clickType.isItemMoveable) {
            inventory.updateWindowItems(e.viewer)
        }
    }

    override fun handleDrag(e: WindowDragEvent) {
        e.isCancelled = true
    }

    override fun handleItemsCollect(e: WindowItemsCollectEvent) {
        e.isCancelled = true
    }

    override fun handleItemsMove(e: WindowItemsMoveEvent) {
        e.isCancelled = true
    }


}