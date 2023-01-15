package cc.trixey.invero.impl

import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherFunction
import taboolib.module.kether.KetherShell

/**
 * Invero
 * cc.trixey.invero.impl.KetherHandler
 *
 * @author Arasple
 * @since 2023/1/14 14:20
 */
object InveroKetherHandler {

    private val namespace = listOf("invero")
    fun invoke(source: String, player: Player?, vars: Map<String, Any>) = KetherShell.eval(
        source,
        sender = if (player != null) adaptPlayer(player) else console(),
        namespace = namespace,
        vars = KetherShell.VariableMap(vars)
    )

    fun parseInline(source: String, player: Player?, vars: Map<String, Any>) = KetherFunction.parse(
        source,
        sender = if (player != null) adaptPlayer(player) else console(),
        namespace = namespace,
        vars = KetherShell.VariableMap(vars)
    )

}