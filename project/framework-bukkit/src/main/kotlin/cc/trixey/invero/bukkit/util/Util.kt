package cc.trixey.invero.bukkit.util

import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit

/**
 * Invero
 * cc.trixey.invero.bukkit.util.Util
 *
 * @author Arasple
 * @since 2022/12/30 13:02
 */
inline fun synced(crossinline block: () -> Unit) {
    if (isPrimaryThread) block()
    else submit { block() }
}

fun Boolean.proceed(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

fun Boolean.elseProceed(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}

fun <T> T.proceed(condition: (T) -> Boolean, block: () -> Unit): T {
    if (condition(this)) block()
    return this
}

fun middle(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Pair<Int, Int> {
    return (pos1.first + pos2.first) / 2 to (pos1.second + pos2.second) / 2
}