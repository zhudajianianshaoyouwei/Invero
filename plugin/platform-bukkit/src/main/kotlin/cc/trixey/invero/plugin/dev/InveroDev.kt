package cc.trixey.invero.plugin.dev

import cc.trixey.invero.serialize.hocon.HoconLoader
import cc.trixey.invero.serialize.hocon.PatchIncluder
import cc.trixey.invero.serialize.toJson
import cc.trixey.invero.serialize.toMenu
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.bukkitPlugin
import java.io.File

/**
 * Invero
 * cc.trixey.invero.plugin.dev.InveroDev
 *
 * @author Arasple
 * @since 2023/1/15 15:44
 */
@CommandHeader(name = "invero")
object InveroDev {

    @CommandBody
    val printHocon = subCommand {
        execute<CommandSender> { _, _, _ ->

            File(bukkitPlugin.dataFolder, "workspace")
                .listFiles()
                ?.filter { it.extension == "conf" }
                ?.map { file ->
                    val patched = PatchIncluder.patchHoconFile(file)
                    val config = Configuration.loadFromString(patched, Type.HOCON)
                    config.file = file
                    config
                }
                ?.forEach {
                    println("------------------------")
                    println(it.saveToString())
                }

        }

    }

    @CommandBody
    val main = subCommand {
        execute<Player> { player, _, _ ->
            Configuration.loadFromString("")

            val conf = File(bukkitPlugin.dataFolder, "workspace/main.conf").let { HoconLoader.loadFromFile(it) }
            kotlin.runCatching {
                val menu = conf.toMenu()

                println(
                    """
                    Loaded Menu ---------------------------------->
                    ${menu.toJson()}
                    < ----------------------------------
                """.trimIndent()
                )

                menu.open(player)
            }.onFailure { throwable ->
                println("§c" + throwable.localizedMessage)
                throwable.stackTrace.forEach {
                    println("§8$it")
                }
            }
        }

    }

}