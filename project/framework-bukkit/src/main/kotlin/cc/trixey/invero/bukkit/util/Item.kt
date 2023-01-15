package cc.trixey.invero.bukkit.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.*
import java.util.*

/**
 * Invero
 * cc.trixey.invero.bukkit.util.Item
 *
 * @author Arasple
 * @since 2023/1/5 14:35
 */
private val namespacedKey = NamespacedKey(BukkitPlugin.getInstance(), "invero_slot")

fun ItemStack.searchHeadTexture(): String? {
    val meta = itemMeta as SkullMeta? ?: return null

    return meta
        .getProperty<GameProfile>("profile")
        ?.properties
        ?.values()
        ?.find {
            it.name == "textures"
        }
        ?.value
}

fun Player.getPlayerTexture(): String {
    return invokeMethod<GameProfile>("getProfile")
        ?.properties
        ?.get("textures")
        ?.find { it.value != null }!!.value
}

fun ItemStack.modifyHeadTexture(input: String): ItemStack {
    val profile = GameProfile(UUID.randomUUID(), null)
    val texture =
        if (input.length in 60..100)
            Base64
                .getEncoder()
                .encodeToString("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/$input\"}}}".toByteArray())
        else input

    profile.properties.put("textures", Property("textures", texture, "Invero_TexturedSkull"))

    return modifyMeta<SkullMeta> { setProperty("profile", profile) }
}

fun ItemStack.distinguish(mark: Byte = (Byte.MIN_VALUE..Byte.MAX_VALUE).random().toByte()): ItemStack {
    return modifyMeta<ItemMeta> {
        persistentDataContainer.set(namespacedKey, PersistentDataType.BYTE, mark)
    }
}

fun randomItem(builder: ItemBuilder.() -> Unit = {}): ItemStack {
    var itemStack: ItemStack? = null
    while (itemStack.isAir()) {
        itemStack = ItemStack(Material.values().filter { it.isItem || it.isBlock }.random())
    }
    return buildItem(itemStack!!, builder).also {
        it.amount = it.amount.coerceAtMost(it.maxStackSize)
    }
}

fun randomMaterial(): Material {
    return Material.values().filter { (it.isItem || it.isBlock) && it.isNotAir() }.random()
}