package cc.trixey.invero.bukkit.window

import cc.trixey.invero.bukkit.PacketInventory
import cc.trixey.invero.bukkit.PacketWindow
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.common.ContainerType

/**
 * Invero
 * cc.trixey.invero.bukkit.window.PacketChestWindow
 *
 * @author Arasple
 * @since 2023/1/12 16:04
 */
class PacketChestWindow(
    rows: Int,
    title: String,
    storageMode: StorageMode
) : PacketWindow(ContainerType.ofRows(rows.coerceAtMost(6)), storageMode, title) {

    override val scale = Scale(9 to rows + if (storageMode.overridePlayerInventory) 4 else 0)

    override val inventory = PacketInventory(this)

}
