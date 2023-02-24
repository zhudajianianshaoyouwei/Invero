package cc.trixey.invero.ui.bukkit.api.dsl

import cc.trixey.invero.ui.bukkit.*
import cc.trixey.invero.ui.common.ContainerType
import cc.trixey.invero.ui.common.ContainerType.ANVIL
import cc.trixey.invero.ui.common.StorageMode
import taboolib.common.platform.function.warning

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.api.dsl.Window
 *
 * @author Arasple
 * @since 2023/1/5 13:29
 */
inline fun commonWindow(
    viewer: PlayerViewer,
    title: String = "Untitled",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true,
    type: ContainerType,
    block: BukkitWindow.() -> Unit = {}
): BukkitWindow {
    return if (type.isOrdinaryChest) {
        chestWindow(viewer, type.rows, title, storageMode, virtual, block)
    } else when (type) {
        ANVIL -> WindowAnvil(viewer, title, storageMode, virtual).also(block)
        else -> WindowDefault(viewer, type, title, storageMode, virtual).also(block).also {
            warning("You are using an unfully supported window type: $type")
        }
    }
}

inline fun chestWindow(
    viewer: PlayerViewer,
    rows: Int,
    title: String = "Untitled",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true,
    block: WindowChest.() -> Unit = {}
): WindowChest {
    return WindowChest(viewer, rows, title, storageMode, virtual).also(block)
}