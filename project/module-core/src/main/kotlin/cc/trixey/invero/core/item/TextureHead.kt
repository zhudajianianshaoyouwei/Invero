package cc.trixey.invero.core.item

import cc.trixey.invero.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.item.TextureHead
 *
 * @author Arasple
 * @since 2023/1/16 10:36
 */
@Serializable
class TextureHead(@SerialName("head") override val raw: String) : Texture() {

    override fun generateItem(session: Session): CompletableFuture<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun isStatic(): Boolean {
        return false
    }

}