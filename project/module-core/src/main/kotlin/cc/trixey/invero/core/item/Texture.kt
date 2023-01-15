package cc.trixey.invero.core.item

import cc.trixey.invero.core.menu.MenuSession
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.item.Texture
 *
 * @author Arasple
 * @since 2023/1/14 14:13
 */
interface Texture {

    val value: String

    fun generateItem(session: MenuSession): CompletableFuture<ItemStack>

    companion object {

        val DEFAULT_TEXTURE = ItemStack(Material.STONE)

    }

}