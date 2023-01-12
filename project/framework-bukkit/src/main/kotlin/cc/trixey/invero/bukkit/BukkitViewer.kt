package cc.trixey.invero.bukkit

import cc.trixey.invero.common.Viewer
import org.bukkit.entity.Player
import taboolib.common.platform.function.getProxyPlayer
import java.util.*

/**
 * Invero
 * cc.trixey.invero.bukkit.BukkitViewer
 *
 * @author Arasple
 * @since 2022/12/29 13:11
 */
@JvmInline
value class BukkitViewer(override val uuid: UUID) : Viewer {

    fun getSafe() = getProxyPlayer(uuid)?.cast<Player>()

    fun get() = getSafe()!!

    constructor(player: Player) : this(player.uniqueId)

    override fun isAvailable(): Boolean {
        return getSafe() != null
    }

    override fun <T> getInstance(): T {
        return getProxyPlayer(uuid)!!.cast()
    }

}