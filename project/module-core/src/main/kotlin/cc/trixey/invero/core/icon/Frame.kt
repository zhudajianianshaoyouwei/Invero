package cc.trixey.invero.core.icon

import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.serialize.ListScoping
import kotlinx.serialization.Serializable

/**
 * Invero
 * cc.trixey.invero.core.icon.Frame
 *
 * @author Arasple
 * @since 2023/1/16 11:51
 */
@Serializable
class Frame(
    val delay: Long? = null,
    val texture: Texture? = null,
    val name: String? = null,
    @Serializable(with = ListScoping::class)
    val lore: List<String>? = null,
    val amount: Int? = null,
    @Serializable(with = ListScoping::class)
    val slot: List<Slot>? = null,
)