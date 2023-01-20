package cc.trixey.invero.core

import cc.trixey.invero.common.StorageMode
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*

/**
 * Invero
 * cc.trixey.invero.core.MenuOptions
 *
 * @author Arasple
 * @since 2023/1/15 20:54
 */
@Serializable
class MenuOptions(
    @SerialName("override-player-inventory")
    val overridePlayerInventory: Boolean = true,
    @SerialName("hide-player-storage")
    val hidePlayerStorage: Boolean = false
) {

    @Transient
    val storageMode = StorageMode(overridePlayerInventory, hidePlayerStorage)

}