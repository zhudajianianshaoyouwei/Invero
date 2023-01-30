package cc.trixey.invero.core.util

import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherFunction
import taboolib.module.kether.KetherShell

/**
 * Invero
 * cc.trixey.invero.core.util.KetherHandler
 *
 * @author Arasple
 * @since 2023/1/16 13:14
 */
object KetherHandler {

    private val namespace = listOf("invero")

    fun invoke(source: String, player: Player?, vars: Map<String, Any>) = KetherShell.eval(
        source,
        sender = if (player != null) adaptPlayer(player) else console(),
        namespace = namespace,
        vars = KetherShell.VariableMap(vars)
    )

    fun parseInline(source: String, player: Player?, vars: Map<String, Any>) = kotlin.runCatching {
        KetherFunction.parse(
            source,
            sender = if (player != null) adaptPlayer(player) else console(),
            namespace = namespace,
            vars = KetherShell.VariableMap(vars)
        )
    }.onFailure {
        it.prettyPrint()
    }.getOrElse { "§c[ERROR: $source]" }

}