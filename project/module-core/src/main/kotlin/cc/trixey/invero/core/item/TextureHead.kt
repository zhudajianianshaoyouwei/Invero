package cc.trixey.invero.core.item

import cc.trixey.invero.core.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.XSkull

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
    override val lazyTexture: ItemStack? = run {
        if (containsPlaceholder) null
        else generate(raw)
    }

    override fun generateItem(session: Session, delayedItem: (ItemStack) -> Unit): ItemStack {
        return lazyTexture ?: generate(session.parse(raw))
    }

    private fun generate(identifier: String): ItemStack {
        val builder = defaultHead.clone()
        val meta = builder.itemMeta!!

        builder.itemMeta = XSkull.applySkin(meta, identifier)
        return builder
    }

}