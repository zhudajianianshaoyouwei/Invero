package cc.trixey.invero.common

/**
 * Invero
 * cc.trixey.invero.common.ProxyInventory
 *
 * @author Arasple
 * @since 2022/12/30 12:56
 */
interface ProxyInventory {

    fun getWindow(): Window

    fun open(viewer: Viewer)

    fun close(viewer: Viewer, updateInventory: Boolean = true)

    fun closeAll()

    fun getContainerSize(): Int

    fun clear()

    fun clear(slots: Collection<Int>)

}