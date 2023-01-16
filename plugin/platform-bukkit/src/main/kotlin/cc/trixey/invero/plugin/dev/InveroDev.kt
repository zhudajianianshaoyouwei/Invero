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
import taboolib.common.platform.function.submit
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
    val tryme = subCommand {
        execute<Player> { player, _, _ ->
            Configuration.loadFromString("")

            val conf = File(bukkitPlugin.dataFolder, "workspace/tryme.conf").let { HoconLoader.loadFromFile(it) }
            val menu = conf.toMenu()

            println(
                """
                    Loaded Menu ---------------------------------->
                    ${menu.toJson()}
                    < ----------------------------------
                """.trimIndent()
            )

            submit(delay = 20L){
                menu.open(player)
            }
        }

    }

}