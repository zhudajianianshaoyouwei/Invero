package cc.trixey.invero.ui.bukkit

import cc.trixey.invero.ui.common.ContainerType
import cc.trixey.invero.ui.common.Scale

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.WindowDefault
 *
 * @author Arasple
 * @since 2023/2/24 13:39
 *
 * 暂未声明的其他容器类型。未来会单独支持。
 * 请注意，无法操作容器的特殊属性
 */
open class WindowDefault(
    final override val type: ContainerType,
    override var title: String,
    viewer: PlayerViewer,
    final override val hidePlayerInventory: Boolean,
    virtual: Boolean
) : BukkitWindow(type, title, viewer, hidePlayerInventory, virtual) {

    override val scale = Scale(9 to type.rows + if (hidePlayerInventory) 4 else 0)

    override val inventory: ProxyBukkitInventory by lazy {
        if (virtual) InventoryPacket(this)
        else InventoryVanilla(this)
    }

}