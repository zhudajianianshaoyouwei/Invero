package cc.trixey.invero.bukkit.window

import cc.trixey.invero.bukkit.BukkitInventory
import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.common.WindowType
import org.bukkit.Bukkit

/**
 * Invero
 * cc.trixey.invero.bukkit.window.BukkitChestWindow
 *
 * @author Arasple
 * @since 2022/12/29 12:51
 */
class BukkitChestWindow(
    rows: Int,
    title: String,
    storageMode: StorageMode
) : BukkitWindow(WindowType.ofRows(rows.coerceAtMost(6)), storageMode, title) {

    override val scale = Scale(9 to rows + if (storageMode.overridePlayerInventory) 4 else 0)

    override val inventory = BukkitInventory(
        this,
        Bukkit.createInventory(
            BukkitWindowHolder(this), type.containerSize, title
        )
    )

}