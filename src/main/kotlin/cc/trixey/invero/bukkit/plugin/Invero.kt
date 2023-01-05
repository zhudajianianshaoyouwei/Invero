package cc.trixey.invero.bukkit.plugin

import taboolib.common.platform.Plugin
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
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
        val basic = subCommand {
            execute { sender, _, _ ->
                UnitBasic.show(sender)
            }
        }

    }

}