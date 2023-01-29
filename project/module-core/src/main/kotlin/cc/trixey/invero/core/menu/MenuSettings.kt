package cc.trixey.invero.core.menu

import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.common.StorageMode
import cc.trixey.invero.core.serialize.InventoryTypeSerializer
import cc.trixey.invero.core.serialize.MenuTitleSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.event.inventory.InventoryType

/**
 * Invero
 * cc.trixey.invero.core.menu.MenuSettings
 *
 * @author Arasple
 * @since 2023/1/25 11:35
 */
@Serializable
class MenuSettings(
    @Serializable(with = MenuTitleSerializer::class)
    val title: MenuTitle,
    var rows: Int?,
    val virtual: Boolean = false,
    @Serializable(with = InventoryTypeSerializer::class)
    val type: InventoryType = InventoryType.CHEST,
    @SerialName("override-player-inventory")
    val overridePlayerInventory: Boolean = true,
    @SerialName("hide-player-storage")
    val hidePlayerStorage: Boolean = false,
) {

    val containerType by lazy {
        if (type != InventoryType.CHEST) ContainerType.fromBukkitType(type.name)
        else ContainerType.ofRows(rows ?: 6)
    }

    @Transient
    val storageMode = StorageMode(overridePlayerInventory, hidePlayerStorage)

}