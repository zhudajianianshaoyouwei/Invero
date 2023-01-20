package cc.trixey.invero.bukkit.api.dsl

import cc.trixey.invero.bukkit.PlayerViewer
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.bukkit.api.dsl.Viewer
 *
 * @author Arasple
 * @since 2023/1/20 15:55
 */
fun String.toPlayerViewer(): PlayerViewer {
    return PlayerViewer(this)
}

fun Player.toPlayerViewer(): PlayerViewer {
    return PlayerViewer(this)
}