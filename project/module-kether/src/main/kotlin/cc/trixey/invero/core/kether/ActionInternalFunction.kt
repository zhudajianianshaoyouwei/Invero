package cc.trixey.invero.core.kether

import cc.trixey.invero.core.BaseMenu
import cc.trixey.invero.core.util.KetherHandler
import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.kether.ActionInternalFunction
 *
 * @author Arasple
 * @since 2023/2/2 14:14
 */
object ActionInternalFunction {

    @KetherParser(["invoke", "innerscript"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(
            // 节点名称
            text(),
            command("with", then = action()).option().defaultsTo(null)
        ).apply(it) { node, with ->
            future {
                val session = session()
                val menu = session?.menu as? BaseMenu ?: return@future CompletableFuture.completedFuture(false)
                val player = session.viewer.get<Player>()


                val params = with?.let { w -> newFrame(w).run<Any>().getNow(null) }.toParameter()
                val script = menu.scripts?.get(node) ?: return@future CompletableFuture.completedFuture(false)

                return@future KetherHandler
                    .invoke(script, player, session.getVariables(mapOf("invokeArgs" to params)))
            }
        }
    }

    private fun Any?.toParameter(): List<String> = when (this) {
        null -> emptyList()
        is Collection<*> -> map { it.toString() }
        else -> listOf(toString())
    }

}