package cc.trixey.invero.ui.bukkit.util

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.BukkitPlugin

/**
 * Invero
 * cc.trixey.invero.ui.bukkit.util.PDC
 *
 * @author Arasple
 * @since 2023/2/26 18:14
 */
internal val namespacedKey = NamespacedKey(BukkitPlugin.getInstance(), "invero")

fun Player.setMeta(key: String, value: Any) {
    persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, value.toString())
}