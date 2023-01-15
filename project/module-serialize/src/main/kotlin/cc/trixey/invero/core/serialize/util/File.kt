package cc.trixey.invero.core.serialize.util

import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File

/**
 * Invero
 * cc.trixey.invero.core.serialize.util.File
 *
 * @author Arasple
 * @since 2023/1/14 21:18
 */
fun File.listRecursively(): List<File> {
    val result = mutableListOf<File>()

    if (!isDirectory) {
        result += this
        return result
    } else {
        listFiles()?.forEach { result += it.listRecursively() }
    }

    return result
}

fun File.isHocon(): Boolean {
    return Configuration.getTypeFromFile(this) == Type.HOCON
}