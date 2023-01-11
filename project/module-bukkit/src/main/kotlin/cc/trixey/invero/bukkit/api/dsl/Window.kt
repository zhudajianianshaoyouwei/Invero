package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.window.BukkitChestWindow
import cc.trixey.invero.common.StorageMode

/**
 * Invero
 * cc.trixey.invero.bukkit.api.dsl.Window
 *
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun bukkitChestWindow(
    rows: Int,
    title: String,
    storageMode: StorageMode = StorageMode(),
    block: BukkitChestWindow.() -> Unit
): BukkitChestWindow {
    return BukkitChestWindow(rows, title, storageMode).also(block)
}