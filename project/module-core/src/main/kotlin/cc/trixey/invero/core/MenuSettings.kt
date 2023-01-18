package cc.trixey.invero.core

import cc.trixey.invero.common.ContainerType
import cc.trixey.invero.core.serialize.InventoryTypeSerializer
import cc.trixey.invero.core.serialize.MenuTitleSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.event.inventory.InventoryType

/**
 * Invero
 * cc.trixey.invero.core.MenuSettings
 *
 * @author Arasple
 * @since 2023/1/15 20:42
 */
@Serializable
class MenuSettings(
    @Serializable(with = MenuTitleSerializer::class)
    val title: MenuTitle,
    val rows: Int,
    @Serializable(with = InventoryTypeSerializer::class)
    val type: InventoryType = InventoryType.CHEST,
    val options: MenuOptions = MenuOptions()
) {

    @Transient
    val containerType = let {
        if (type != InventoryType.CHEST) ContainerType.fromBukkitType(type.name)
        else ContainerType.ofRows(rows ?: 6)
    }

}