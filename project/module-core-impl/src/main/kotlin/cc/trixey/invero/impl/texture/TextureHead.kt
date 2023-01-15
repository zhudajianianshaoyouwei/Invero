package cc.trixey.invero.impl.texture

import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.item.Texture
import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.impl.item.TextureHead
 *
 * @author Arasple
 * @since 2023/1/15 11:50
 */
class TextureHead(override val value: String) : Texture {

    override fun generateItem(session: MenuSession): CompletableFuture<ItemStack> {
        TODO("Not yet implemented")
    }

}