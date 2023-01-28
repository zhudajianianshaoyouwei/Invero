package cc.trixey.invero.core.item

import cc.trixey.invero.core.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.item.TextureSource
 *
 * @author Arasple
 * @since 2023/1/19 16:33
 */
@Serializable
class TextureSource(@SerialName("source") override val raw: String, val value: String) : Texture() {

    override fun generateItem(session: Session, delayedItem: (ItemStack) -> Unit): ItemStack {
        TODO("Not yet implemented")
    }

    override fun isStatic(): Boolean {
        TODO("Not yet implemented")
    }

}