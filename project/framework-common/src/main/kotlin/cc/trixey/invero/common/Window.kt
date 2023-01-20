package cc.trixey.invero.common

/**
 * Invero
 * cc.trixey.invero.common.Window
 *
 * @author Arasple
 * @since 2022/12/20 20:04
 */
interface Window : Gridable {

    val viewer: Viewer

    var title: String

    val type: ContainerType

    val storageMode: StorageMode

    val inventory: ProxyInventory

    fun open()

    fun render()

    fun close(doCloseInventory: Boolean = true, updateInventory: Boolean = true)

    fun isViewing(): Boolean

}