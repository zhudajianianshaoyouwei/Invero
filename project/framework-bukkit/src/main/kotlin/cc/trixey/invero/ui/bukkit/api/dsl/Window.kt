package cc.trixey.invero.ui.bukkit.api.dsl

import cc.trixey.invero.ui.bukkit.PlayerViewer
import cc.trixey.invero.ui.bukkit.impl.ChestWindow
import cc.trixey.invero.ui.common.StorageMode
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.api.dsl.Window
 *
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun chestWindow(
    viewer: PlayerViewer,
    rows: Int,
    title: String = "Untitled",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true,
    block: ChestWindow.() -> Unit = {}
): ChestWindow {
    return ChestWindow(viewer, rows, title, storageMode, virtual).also(block)
}

inline fun chestWindow(
    viewer: Player,
    rows: Int,
    title: String = "Untitled",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true,
    block: ChestWindow.() -> Unit = {}
) = chestWindow(PlayerViewer(viewer), rows, title, storageMode, virtual, block)