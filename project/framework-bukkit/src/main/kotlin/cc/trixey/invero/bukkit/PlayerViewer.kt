package cc.trixey.invero.bukkit

import cc.trixey.invero.common.Viewer
import org.bukkit.entity.Player
import taboolib.common.platform.function.getProxyPlayer

/**
 * Invero
 * cc.trixey.invero.bukkit.PlayerViewer
 *
 * @author Arasple
 * @since 2023/1/20 13:35
 */
class PlayerViewer(override val name: String) : Viewer {

    constructor(player: Player) : this(player.name)

    override fun isAvailable(): Boolean {
        return get<Player>().isOnline
    }

    override fun <T> get(): T {
        return getProxyPlayer(name)?.cast() ?: error("Not found available player for viewer named $name")
    }

}