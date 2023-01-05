package cc.trixey.invero.bukkit.plugin

import cc.trixey.invero.bukkit.plugin.unit.showBasic
import cc.trixey.invero.bukkit.plugin.unit.showRunningApple
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
object Invero : Plugin() {

    override fun onLoad() {
        info("Loaded")
    }

    @CommandHeader(name = "invero", aliases = ["inv"])
    object Handler {

        @CommandBody
        val basic = construct { showBasic(this) }

        @CommandBody
        val basic_dynamicItem = construct { showRunningApple(this) }

        private fun construct(block: Player.() -> Unit): SimpleCommandBody {
            return subCommand {
                execute { sender, _, _ ->
                    block(sender)
                }
            }
        }

    }

}