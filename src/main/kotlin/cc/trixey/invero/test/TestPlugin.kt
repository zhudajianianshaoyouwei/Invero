package cc.trixey.invero.test

import cc.trixey.invero.test.unit.showBasic
import cc.trixey.invero.test.unit.showDynamicTitle
import cc.trixey.invero.test.unit.showFreeformStandard
import cc.trixey.invero.test.unit.showRunningApple
import org.bukkit.entity.Player
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.info

/**
 * @author Arasple
 * @since 2022/12/20 20:42
 */
object TestPlugin : Plugin() {

    override fun onLoad() {
        info("Loaded")
    }

    @CommandHeader(name = "invero", aliases = ["inv"])
    object Handler {

        @CommandBody
        val basic = construct { showBasic(this) }

        @CommandBody
        val basic_dynamicItem = construct { showRunningApple(this) }

        @CommandBody
        val basic_dynamicTitle = construct { showDynamicTitle(this) }

        @CommandBody
        val freeform = construct { showFreeformStandard(this) }

        private fun construct(block: Player.() -> Unit): SimpleCommandBody {
            return subCommand {
                execute { sender, _, _ ->
                    block(sender)
                }
            }
        }

    }

}