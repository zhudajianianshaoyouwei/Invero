package cc.trixey.invero.plugin.unit

import cc.trixey.invero.core.serialize.hocon.PatchIncluder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.bukkitPlugin
import java.io.File

/**
 * Invero
 * cc.trixey.invero.plugin.unit.UnitCommand
 *
 * @author Arasple
 * @since 2023/1/12 21:10
 */
@CommandHeader(name = "invero_demo", aliases = ["invdemo"])
object UnitCommand {

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
    val basic = construct { showBasic(this) }

    @CommandBody
    val basic_dynamicItem = construct { showRunningItem(this) }

    @CommandBody
    val basic_dynamicTitle = construct { showDynamicTitle(this) }

    @CommandBody
    val freeform_standard = construct { showFreeformStandard(this) }

    @CommandBody
    val freeform_gameoflife = construct { showTheGameOfLife(this) }

    @CommandBody
    val freeform_netesed = construct { showFreeformNetesed(this) }

    @CommandBody
    val scroll = construct { showScrollStandard(this) }

    @CommandBody
    val scroll_2 = construct { showScroll2(this) }

    @CommandBody
    val generator_paged = construct { showGeneratorPaged(this, it) }

    @CommandBody
    val generator_scroll = construct { showGeneratorScroll(this) }

    @CommandBody
    val IO_storage = construct { showIOStoragePanel(this) }

    private fun construct(block: Player.(String?) -> Unit): SimpleCommandBody {
        return subCommand {
            execute { sender, _, args ->
                block(sender, args.split(" ").getOrNull(1))
            }
        }
    }

}
