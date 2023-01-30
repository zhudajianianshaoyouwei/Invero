package cc.trixey.invero.core.item

import cc.trixey.invero.bukkit.util.requestHead
import cc.trixey.invero.core.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial

/**
 * Invero
 * cc.trixey.invero.core.item.TextureHead
 *
 * @author Arasple
 * @since 2023/1/16 10:36
 */
@Serializable
class TextureHead(@SerialName("head") override val raw: String) : Texture() {

    @Transient
    private val defaultHead = XMaterial.PLAYER_HEAD.parseItem()!!

    @Transient
    override var lazyTexture: ItemStack? = null

    override fun generateItem(context: Context, block: (ItemStack) -> Unit) {
        if (lazyTexture != null) return block(lazyTexture!!)
        if (!containsPlaceholder && raw.length > 20) requestHead(raw) { lazyTexture = it.also(block) }
        else {
            requestHead(context.parse(raw)) { block(it.clone()) }
        }
    }

}