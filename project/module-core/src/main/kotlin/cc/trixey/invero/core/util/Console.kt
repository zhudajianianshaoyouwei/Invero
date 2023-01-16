package cc.trixey.invero.core.util

import taboolib.platform.util.bukkitPlugin
import java.io.File

/**
 * Invero
 * cc.trixey.invero.core.util.Console
 *
 * @author Arasple
 * @since 2023/1/16 14:50
 */
fun isDebugEnable(): Boolean {
    return File(bukkitPlugin.dataFolder, "dev").exists()
}

fun debug(message: String) {
    if (isDebugEnable())
        println("§c[Invero] §7${message.replace('&', '§')}")
}