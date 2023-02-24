package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.common.ContainerType
import cc.trixey.invero.ui.common.StorageMode

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.WindowChest
 *
 * @author Arasple
 * @since 2023/1/20 13:13
 */
class WindowChest(
    viewer: PlayerViewer,
    rows: Int = 6,
    title: String = "Untitled_Chest",
    storageMode: StorageMode = StorageMode(),
    virtual: Boolean = true
) : WindowDefault(viewer, ContainerType.ofRows(rows), title, storageMode, virtual)