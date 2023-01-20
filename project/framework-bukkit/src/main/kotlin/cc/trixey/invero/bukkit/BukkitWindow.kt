package cc.trixey.invero.bukkit

import cc.trixey.invero.bukkit.api.findWindow
import cc.trixey.invero.bukkit.api.register
import cc.trixey.invero.bukkit.api.unregister
import cc.trixey.invero.bukkit.nms.updateTitle
import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.common.Window
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.bukkit.BukkitWindow
 *
 * @author Arasple
 * @since 2023/1/20 12:15
 */
abstract class BukkitWindow(
    override val viewer: PlayerViewer,
    override val type: ContainerType,
    title: String = "Invero_Untitled",
    override val storageMode: StorageMode
) : Window, PanelContainer {

    override var title: String = title
        set(value) {
            field = value
            submit { updateTitle(value) }
        }

    override val panels = arrayListOf<BukkitPanel>()

    override val scale: Scale by lazy { Scale(9 to 6) }

    private var closeCallback: (BukkitWindow) -> Unit = { _ -> }

    private var openCallback: (BukkitWindow) -> Unit = { _ -> }

    private var postOpenCallback: (BukkitWindow) -> Boolean = { _ -> true }

    private var postCloseCallback: (BukkitWindow) -> Unit = { _ -> }

    abstract override val inventory: ProxyBukkitInventory

    fun onClose(block: (BukkitWindow) -> Unit): BukkitWindow {
        closeCallback = block
        return this
    }

    fun onOpen(block: (BukkitWindow) -> Unit): BukkitWindow {
        openCallback = block
        return this
    }

    fun postOpen(block: (BukkitWindow) -> Boolean): BukkitWindow {
        postOpenCallback = block
        return this
    }

    fun postClose(block: (BukkitWindow) -> Unit): BukkitWindow {
        postCloseCallback = block
        return this
    }

    override fun open() {
        // 如果被取消
        if (!postOpenCallback(this)) return
        // 正在查看一个 Window，则伪关闭
        findWindow(viewer.name)?.close(doCloseInventory = false, updateInventory = false)
        // 开启新容器
        register()
        inventory.open()
        render()
    }

    override fun close(doCloseInventory: Boolean, updateInventory: Boolean) {
        postCloseCallback(this)
        unregister()
        inventory.close(doCloseInventory, updateInventory)
        closeCallback(this)
    }

    override fun render() {
        require(panels.all { it.parent == this })

        panels
            .sortedByDescending { it.weight }
            .forEach { it.render() }
    }

    override fun isViewing(): Boolean {
        return inventory.isViewing()
    }

}