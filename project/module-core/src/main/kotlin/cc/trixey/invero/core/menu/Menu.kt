package cc.trixey.invero.core.menu

import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.common.Viewer
import cc.trixey.invero.core.animation.CycleMode

/**
 * Invero
 * cc.trixey.invero.core.menu.Menu
 *
 * @author Arasple
 * @since 2023/1/14 16:17
 */
interface Menu {

    /**
     * The title of this menu
     */
    val title: Array<String>

    /**
     * The update frequency of animated titles
     */
    val titleFramePeriod: Int

    /**
     * The cycle mode of title animation
     */
    val titleFrameMode: CycleMode

    /**
     * The container type this menu presents
     */
    val type: ContainerType

    /**
     * Storage mode (hide player inventory or not)
     */
    val storageMode: StorageMode

    /**
     * Menu options
     */
    val options: MenuOptions

    fun open(viewer: Viewer)

    fun close(viewer: Viewer)

}