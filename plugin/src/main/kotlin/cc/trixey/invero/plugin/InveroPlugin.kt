package cc.trixey.invero.plugin

import cc.trixey.invero.plugin.unit.*
import org.bukkit.entity.Player
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.info

/**
 * Invero
 * cc.trixey.invero.plugin.InveroPlugin
 *
 * @author Arasple
 * @since 2022/12/20 20:42
 */
object InveroPlugin : Plugin() {

    override fun onLoad() {
        info("Loaded")
    }

    @CommandHeader(name = "invero", aliases = ["inv"])
    object Handler {

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
        val generator_paged = construct { showGeneratorPaged(this) }

        private fun construct(block: Player.(String) -> Unit): SimpleCommandBody {
            return subCommand {
                execute { sender, _, s ->
                    println(s)
                    block(sender,s)
                }
            }
        }

    }

}