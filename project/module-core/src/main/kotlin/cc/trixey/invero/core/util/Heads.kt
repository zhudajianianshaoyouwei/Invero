package cc.trixey.invero.core.util

import cc.trixey.invero.bukkit.util.getPlayerTexture
import cc.trixey.invero.bukkit.util.modifyHeadTexture
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.net.URL
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.util.Heads
 *
 * @author Arasple
 * @since 2023/1/15 12:28
 */
object Heads {

    private val MOJANG_API = arrayOf(
        "https://api.mojang.com/users/profiles/minecraft/",
        "https://sessionserver.mojang.com/session/minecraft/profile/"
    )

    private val DEFAULT_HEAD = XMaterial.PLAYER_HEAD.parseItem()!!
    private val CACHED_PLAYER_TEXTURE = mutableMapOf<String, String>()
    private val CACHED_SKULLS = mutableMapOf<String, ItemStack>()

    fun getCustomTexturedHead(texture: String): ItemStack {
        return CACHED_SKULLS.computeIfAbsent(texture) { DEFAULT_HEAD.clone().modifyHeadTexture(it) }
    }

    fun getPlayerHead(playerId: String): ItemStack {
        val player = Bukkit.getPlayer(playerId)

        if (player?.isOnline == true && !CACHED_PLAYER_TEXTURE.containsKey(playerId)) {
            CACHED_PLAYER_TEXTURE[playerId] = player.getPlayerTexture()
        }

        CACHED_PLAYER_TEXTURE[playerId]
            ?.let { return getCustomTexturedHead(it) }

        return DEFAULT_HEAD
    }

    fun getPlayerTexture(playerId: String): CompletableFuture<String> {
        val readFromURL =
            fromURL("${MOJANG_API[0]}$playerId") ?: error("§7[§3Texture§7] Texture player $playerId not found.")
        val profile = Configuration.loadFromString(readFromURL, Type.JSON)
        val uuid = profile.getString("id")

//        (Configuration.loadFromString(fromURL("${MOJANG_API[1]}$uuid")!!, Type.JSON).getList("properties")
//            ?.forEach {
//                if ("textures" == it.asMap()["name"].toString()) {
//                    CACHED_PLAYER_TEXTURE[playerId] = it.asMap()["value"].toString()
//                    return@forEach
//                }
//            }
        return CompletableFuture()
    }

    private fun fromURL(url: String): String? {
        return try {
            String(URL(url).openStream().readBytes())
        } catch (t: Throwable) {
            t.printStackTrace()
            null
        }
    }

}