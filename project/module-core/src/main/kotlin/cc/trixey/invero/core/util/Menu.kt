package cc.trixey.invero.core.util

import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.icon.IconElement
import cc.trixey.invero.core.icon.Slot
import taboolib.platform.util.bukkitPlugin
import java.io.File

/**
 * Invero
 * cc.trixey.invero.core.util.Menu
 *
 * @author Arasple
 * @since 2023/1/16 12:52
 */
fun isDebugEnable(): Boolean {
    return File(bukkitPlugin.dataFolder, "dev").exists()
}

fun debug(message: String) {
    if (isDebugEnable())
        println("§c[Invero] §7${message.replace('&', '§')}")
}

fun List<Slot>.flatRelease(scale: Scale): List<Pos> {
    return flatMap { it.release(scale) }
}

fun IconElement.context(): Context {
    return Context(session, panel, this)
}