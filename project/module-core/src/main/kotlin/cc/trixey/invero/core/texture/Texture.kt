package cc.trixey.invero.core.texture

import cc.trixey.invero.core.Context
import org.bukkit.inventory.ItemStack

/**
 * Invero
 * cc.trixey.invero.core.texture.Texture
 *
 * @author Arasple
 * @since 2023/1/14 14:13
 */
interface Texture {

    fun generateItem(context: Context): ItemStack

}