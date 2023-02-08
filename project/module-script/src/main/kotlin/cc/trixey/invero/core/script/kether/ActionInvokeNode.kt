package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.BaseMenu
import cc.trixey.invero.core.node.Node.Type.TEXT
import cc.trixey.invero.core.node.Node.Type.CONST
import cc.trixey.invero.core.script.session
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionInvokeNode
 *
 * @author Arasple
 * @since 2023/2/2 14:14
 */
object ActionInvokeNode {

    @KetherParser(["invoke", "node"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(
            // 节点名称
            text(),
            // 参数
            command("with", then = action()).option().defaultsTo(null)
        ).apply(it) { nodeName, with ->
            future {
                val session = session()
                val menu = session?.menu as? BaseMenu ?: return@future CompletableFuture.completedFuture(false)
                val params = with
                    ?.let { w -> newFrame(w).run<Any>().getNow(null) }
                    .toParameter()
                    .let { strings -> mapOf("invokeArgs" to strings) }
                val node = menu.nodes?.get(nodeName)
                    ?: return@future CompletableFuture.completedFuture("<NODE: $nodeName> ${menu.nodes?.keys}")

                when (node.type) {
                    CONST -> node.value
                    TEXT -> session.parse(node.value)
                    else -> node.invoke(session, params)
                }.let {
                    CompletableFuture.completedFuture(it)
                }
            }
        }
    }

    private fun Any?.toParameter(): List<String> = when (this) {
        null -> emptyList()
        is Collection<*> -> map { it.toString() }
        else -> listOf(toString())
    }

}