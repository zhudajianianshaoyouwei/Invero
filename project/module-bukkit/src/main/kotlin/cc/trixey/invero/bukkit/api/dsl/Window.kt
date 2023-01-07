package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.window.BukkitChestWindow

/**
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun bukkitChestWindow(rows: Int, title: String, block: BukkitChestWindow.() -> Unit): BukkitChestWindow {
    return BukkitChestWindow(rows, title).also(block)
}