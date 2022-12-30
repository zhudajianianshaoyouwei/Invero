package cc.trixey.invero.bukkit

import cc.trixey.invero.common.WindowType
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @since 2022/12/29 12:51
 */
class BukkitChestWindow(rows: Int, title: String) : BukkitWindow(WindowType.ofRows(rows.coerceAtMost(6)), title) {

    override val inventory: BukkitInventory = BukkitInventory(
        this,
        Bukkit.createInventory(
            BukkitWindowHolder(this), type.containerSize, title
        )
    )

}