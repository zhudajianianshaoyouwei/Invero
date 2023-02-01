package cc.trixey.invero.ui.bukkit.impl

import cc.trixey.invero.ui.bukkit.*
import cc.trixey.invero.ui.common.ContainerType
import cc.trixey.invero.ui.common.Scale
import cc.trixey.invero.ui.common.StorageMode

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.impl.ChestWindow
 *
 * @author Arasple
 * @since 2023/1/20 13:13
 */
class ChestWindow(
    viewer: PlayerViewer,
    rows: Int = 6,
    title: String = "Untitled_Chest",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true
) : BukkitWindow(viewer, cc.trixey.invero.ui.common.ContainerType.ofRows(rows), title, storageMode) {

    override val scale = Scale(9 to rows + if (storageMode.overridePlayerInventory) 4 else 0)

    override val inventory: ProxyBukkitInventory by lazy {
        if (virtual) InventoryPacket(this)
        else InventoryVanilla(this)
    }

}