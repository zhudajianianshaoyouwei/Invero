package cc.trixey.invero.test

import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand

/**
 * @author Arasple
 * @since 2022/12/29 13:25
 */
@CommandHeader(name = "invero", aliases = ["inv"])
object TestHandler {

    @CommandBody
    val basic = subCommand {
        execute { sender, _, _ ->
            UnitBasic.show(sender)
        }
    }

}