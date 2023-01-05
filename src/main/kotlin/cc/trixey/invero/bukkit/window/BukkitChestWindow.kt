package cc.trixey.invero.bukkit.window

import cc.trixey.invero.bukkit.BukkitInventory
import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.BukkitWindowHolder
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.WindowType
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @since 2022/12/29 12:51
 */
class BukkitChestWindow(rows: Int, title: String) : BukkitWindow(WindowType.ofRows(rows.coerceAtMost(6)), title) {

    override val scale: Scale = Scale(9 to rows + 4)

    override val inventory: BukkitInventory = BukkitInventory(
        this,
        Bukkit.createInventory(
            BukkitWindowHolder(this), type.containerSize, title
        )
    )

}