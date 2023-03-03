package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.bukkit.api.findWindow
import cc.trixey.invero.ui.bukkit.api.isRegistered
import cc.trixey.invero.ui.bukkit.api.registerWindow
import cc.trixey.invero.ui.bukkit.api.unregisterWindow
import cc.trixey.invero.ui.bukkit.nms.isTitleUpdating
import cc.trixey.invero.ui.bukkit.nms.updateTitle
import cc.trixey.invero.ui.bukkit.util.synced
import cc.trixey.invero.ui.common.Scale
import cc.trixey.invero.ui.common.StorageMode
import cc.trixey.invero.ui.common.Window
import cc.trixey.invero.ui.common.util.locatingAbsoluteSlot
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.BukkitWindow
 *
 * @author Arasple
 * @since 2023/1/20 12:15
 */
abstract class BukkitWindow(
    override val viewer: PlayerViewer,
    override val type: cc.trixey.invero.ui.common.ContainerType,
    title: String = "Invero_Untitled",
    override val storageMode: StorageMode
) : Window, PanelContainer {

    override var title: String = title
        set(value) {
            field = value
            updateTitle(value)
        }

    final override val panels = arrayListOf<BukkitPanel>()

    override val scale: Scale by lazy { Scale(9 to 6) }

    private var closeCallback: (BukkitWindow) -> Unit = { _ -> }

    private var openCallback: (BukkitWindow) -> Unit = { _ -> }

    private var preOpenCallback: (BukkitWindow) -> Any = { _ -> true }

    private var preCloseCallback: (BukkitWindow) -> Unit = { _ -> }

    private var preRenderCallback: (BukkitWindow) -> Unit = { _ -> }

    abstract override val inventory: ProxyBukkitInventory

    val isPlayerInventoryUsed by lazy {
        panels.any { panel ->
            panel.area.any { panel.locatingAbsoluteSlot(it) >= inventory.containerSize }
        }
    }

    fun onClose(block: (BukkitWindow) -> Unit): BukkitWindow {
        closeCallback = block
        return this
    }

    fun onOpen(block: (BukkitWindow) -> Unit): BukkitWindow {
        openCallback = block
        return this
    }

    fun preOpen(block: (BukkitWindow) -> Any): BukkitWindow {
        preOpenCallback = block
        return this
    }

    fun preClose(block: (BukkitWindow) -> Unit): BukkitWindow {
        preCloseCallback = block
        return this
    }

    fun preRender(block: (BukkitWindow) -> Unit): BukkitWindow {
        preRenderCallback = block
        return this
    }

    override fun open() {
        val player = viewer.get<Player>()

        // 如果被取消
        if (preOpenCallback(this) == false) return
        // 当前未备份物品，则说明是首次打开容器，进行备份
        if (!player.isCurrentlyStored()) {
            player.storePlayerInventory(wipe = storageMode.alwaysClean)
        }
        // 正在查看一个 Window，则伪关闭
        findWindow(viewer.name)?.unregisterWindow()
        // 注册窗口
        registerWindow()
        // 开启新容器
        // 避免更新标题带来的残影
        val invokable: () -> Unit = {
            preRenderCallback(this)
            render()
            inventory.open()
            openCallback(this)
        }
        if (viewer.isTitleUpdating()) submit(delay = 2L) { invokable() }
        else synced { invokable() }
    }

    override fun close(doCloseInventory: Boolean, updateInventory: Boolean) {
        require(isRegistered()) { "Can not close an unregistered window" }
        preCloseCallback(this)

        synced {
            inventory.close(doCloseInventory, updateInventory)
        }

        closeCallback(this)
    }

    override fun render() {
        require(panels.all { it.parent == this })

        panels
            .filterNot { it.skipRender }
            .sortedByDescending { it.weight }
            .forEach { it.render() }
    }

    override fun isViewing(): Boolean {
        return inventory.isViewing()
    }

}