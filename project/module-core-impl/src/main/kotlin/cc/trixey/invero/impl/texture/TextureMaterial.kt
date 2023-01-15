package cc.trixey.invero.impl.texture

import cc.trixey.invero.core.menu.MenuSession
import cc.trixey.invero.core.regex.MATERIAL_ID
import cc.trixey.invero.core.regex.containsAnyPlaceholder
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.item.Texture.Companion.DEFAULT_TEXTURE
import org.bukkit.inventory.ItemStack
import taboolib.common.util.Strings.similarDegree
import taboolib.library.xseries.XMaterial
import java.util.concurrent.CompletableFuture
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

/**
 * Invero
 * cc.trixey.invero.impl.item.TextureMaterial
 *
 * @author Arasple
 * @since 2023/1/15 11:50
 */
class TextureMaterial(override val value: String) : Texture {

    val containsPlaceholder = value.containsAnyPlaceholder()

    val lazyMaterial by lazy {
        if (containsPlaceholder) null
        else generate(value) ?: DEFAULT_TEXTURE
    }

    override fun generateItem(session: MenuSession): CompletableFuture<ItemStack> {
        return CompletableFuture<ItemStack>().apply {
            val texture = if (!containsPlaceholder) lazyMaterial!!
            else generate(session.parse(value)) ?: DEFAULT_TEXTURE

            complete(texture)
        }
    }

    private fun generate(material: String): ItemStack? {
        @Suppress("DEPRECATION", "Minecraft versions 1.8-1.12")
        MATERIAL_ID
            .matchEntire(material)
            ?.let {
                val id = it.groupValues[1].toIntOrNull() ?: 1
                val data = it.groupValues.getOrNull(2)?.toByteOrNull() ?: 0

                return XMaterial.matchXMaterial(id, data).getOrNull()?.parseItem()
            }

        // 1.13+
        val cleanForm = material
            .uppercase()
            .replace('-', '_')
            .replace(" ", "")

        return XMaterial.matchXMaterial(cleanForm).getOrElse {
            XMaterial.values().maxByOrNull { similarDegree(it.name, cleanForm) }
        }?.parseItem()
    }

}