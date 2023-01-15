package cc.trixey.invero.core.serialize

import cc.trixey.invero.core.serialize.hocon.HoconLoader
import cc.trixey.invero.core.serialize.util.isHocon
import cc.trixey.invero.core.serialize.util.listRecursively
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Invero
 * cc.trixey.invero.core.serialize.Serializer
 *
 * @author Arasple
 * @since 2023/1/14 21:16
 */
object Serializer {

    fun loadMenu(serializer: MenuSerializer) {

        getMenuConfigurations(File("")).forEach {

        }
    }

    /**
     * 返回该级目录下所有有效菜单的配置
     */
    fun getMenuConfigurations(workspace: File): List<Configuration> {
        val files = workspace.listRecursively()

        return files.mapNotNull {
            if (it.isHocon()) {
                val config = HoconLoader.loadFromFile(it)
                if (config.contains("menu")) {
                    config
                } else null
            } else null
        }
    }

}