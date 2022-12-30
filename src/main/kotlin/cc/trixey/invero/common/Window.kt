package cc.trixey.invero.common

/**
 * @author Arasple
 * @since 2022/12/20 20:04
 */
interface Window : PanelContainer {

    /**
     * Viewers of this window
     */
    val viewers: Set<Viewer>

    /**
     * Title of this window
     */
    var title: String

    /**
     * for Minecraft inventories:
     *
     * Size = container size + 36(player inventory)
     */
    val size: Int

    /**
     * Proxy inventory
     */
    val inventory: ProxyInventory

    /**
     * Locating slot index
     */
    fun locate(x: Int, y: Int): Int

    /**
     * Locating panels' occupied slots
     */
    fun locatePanel(panel: Panel): Set<Int>

    /**
     * Open this window for a viewer
     */
    fun open(viewer: Viewer)

    /**
     * Close this window
     */
    fun close(viewer: Viewer)

    fun <T : Viewer> forViewers(block: (it: T) -> Unit) = viewers
        .filter { it.isAvailable() }
        .forEach {
            block(it.getInstance())
        }

}