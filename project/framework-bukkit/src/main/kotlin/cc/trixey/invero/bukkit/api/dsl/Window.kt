package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.BukkitWindow
import cc.trixey.invero.bukkit.window.BukkitChestWindow
import cc.trixey.invero.bukkit.window.PacketChestWindow
import cc.trixey.invero.common.StorageMode

/**
 * Invero
 * cc.trixey.invero.bukkit.api.dsl.Window
 *
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun chestWindow(
    packet: Boolean,
    rows: Int,
    title: String,
    storageMode: StorageMode = StorageMode(),
    block: BukkitWindow.() -> Unit = {}
): BukkitWindow {
    return if (packet) packetChestWindow(rows, title, storageMode, block)
    else bukkitChestWindow(rows, title, storageMode, block)
}

inline fun bukkitChestWindow(
    rows: Int,
    title: String,
    storageMode: StorageMode = StorageMode(),
    block: BukkitChestWindow.() -> Unit = {}
): BukkitChestWindow {
    return BukkitChestWindow(rows, title, storageMode).also(block)
}

inline fun packetChestWindow(
    rows: Int,
    title: String,
    storageMode: StorageMode = StorageMode(),
    block: PacketChestWindow.() -> Unit = {}
): PacketChestWindow {
    return PacketChestWindow(rows, title, storageMode).also(block)
}