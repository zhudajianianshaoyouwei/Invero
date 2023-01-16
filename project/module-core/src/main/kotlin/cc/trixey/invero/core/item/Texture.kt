package cc.trixey.invero.core.item

import cc.trixey.invero.Session
import cc.trixey.invero.serialize.SelectorTexture
import kotlinx.serialization.Serializable
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.item.Texture
 *
 * @author Arasple
 * @since 2023/1/16 10:29
 */
@Serializable(with = SelectorTexture::class)
abstract class Texture {

    abstract val raw: String

    abstract fun generateItem(session: Session): CompletableFuture<ItemStack>

    abstract fun isStatic(): Boolean

    companion object {

        val DEFAULT_TEXTURE by lazy {
            XMaterial.STONE.parseItem()
        }

    }

}