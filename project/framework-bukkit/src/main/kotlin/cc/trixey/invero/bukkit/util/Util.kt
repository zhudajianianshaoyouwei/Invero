package cc.trixey.invero.bukkit.util

import cc.trixey.invero.bukkit.BukkitViewer
import cc.trixey.invero.bukkit.PacketListener
import cc.trixey.invero.common.Viewer
import org.bukkit.entity.Player

/**
 * Invero
 * cc.trixey.invero.bukkit.util.Util
 *
 * @author Arasple
 * @since 2022/12/30 13:02
 */
fun Boolean.proceed(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

fun <T> T.proceed(condition: (T) -> Boolean, block: () -> Unit): T {
    if (condition(this)) block()
    return this
}

fun Viewer.toBukkitViewer() = this as BukkitViewer

fun Viewer.safeBukkitPlayer(): Player? = getInstanceSafe<Player>()

fun middle(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Pair<Int, Int> {
    return (pos1.first + pos2.first) / 2 to (pos1.second + pos2.second) / 2
}