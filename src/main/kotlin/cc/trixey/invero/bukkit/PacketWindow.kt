package cc.trixey.invero.bukkit

import cc.trixey.invero.common.WindowType
import cc.trixey.invero.common.event.PacketWindowEvent
import cc.trixey.invero.common.event.WindowEvent

/**
 * @author Arasple
 * @since 2023/1/5 21:28
 */
abstract class PacketWindow(type: WindowType, title: String = "Untitled_Invero_Window") : BaseWindow(type, title) {

    override fun handleEvent(e: WindowEvent) {
        e as PacketWindowEvent

//        when (e.type) {
//            EventType.INVENTORY_OPEN -> handleOpen(e.cast())
//            EventType.INVENTORY_CLOSE -> handleClose(e.cast())
//            EventType.ITEMS_DRAG -> handleDrag(e.cast())
//            EventType.ITEMS_MOVE -> handleItemsMove(e.cast())
//            EventType.ITEMS_COLLECT -> handleItemsCollect(e.cast())
//            EventType.CLICK -> handleClick(e.cast())
//        }
    }

}