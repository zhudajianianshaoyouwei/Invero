package cc.trixey.invero.impl.display

import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.menu.MenuSession
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.platform.util.modifyMeta
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.impl.display.DisplaySimple
 *
 * @author Arasple
 * @since 2023/1/15 12:49
 */
class DisplayCommon(val texture: Texture, val name: String?, val description: List<String>?) {

    fun createDisplay(session: MenuSession): CompletableFuture<ItemStack> {
        return texture
            .generateItem(session)
            .thenApply {
                it.modifyMeta<ItemMeta> {
                    if (name != null) setDisplayName(session.parse(name))
                    if (description != null) lore = session.parse(description)
                }
            }
    }

}