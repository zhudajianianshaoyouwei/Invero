package cc.trixey.invero.core.item

import cc.trixey.invero.core.Session
import cc.trixey.invero.core.item.source.SourceManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.item.TextureSource
 *
 * @author Arasple
 * @since 2023/1/29 15:29
 */
@Serializable
class TextureSource(val source: String, @SerialName("value") override val raw: String) : Texture() {

    private val provider by lazy {
        SourceManager.getProvider(source) ?: error("Invalid source provider: $source")
    }

    @Transient
    override val lazyTexture: ItemStack? = run {
        if (containsPlaceholder) null
        else provider.getItem(raw)
    }

    override fun generateItem(session: Session, delayedItem: (ItemStack) -> Unit): ItemStack {
        return lazyTexture ?: provider.getItem(session.parse(raw)) ?: DEFAULT_TEXTURE
    }

}