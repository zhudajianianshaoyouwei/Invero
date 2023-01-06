package cc.trixey.invero.util

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.common.Viewer
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @since 2022/12/30 13:02
 */
fun Viewer.toBukkitViewer() = this as BukkitViewer

fun Viewer.safeBukkitPlayer(): Player? = getInstanceSafe<Player>()