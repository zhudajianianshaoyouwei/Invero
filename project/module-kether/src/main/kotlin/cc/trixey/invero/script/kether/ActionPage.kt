package cc.trixey.invero.script.kether

import cc.trixey.invero.common.panel.PagedPanel
import cc.trixey.invero.script.kether.PageOperator.*
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.expansion.kether.menu.ActionPage
 *
 * @author Arasple
 * @since 2023/1/19 20:33
 */
class ActionPage(
    val operator: PageOperator,
    val value: ParsedAction<*>? = null,
    val indexs: List<Int>? = null
) : ScriptAction<Int>() {

    override fun run(frame: ScriptFrame): CompletableFuture<Int> {
        println("Running page action: $this")
        val located = if (indexs.isNullOrEmpty()) null else frame.findPanelAt<PagedPanel>(indexs)
        println("A")
        val panel = located ?: frame.findNearstPanel() ?: return CompletableFuture.completedFuture(-1)
        println("B")

        // 输出 Page 值
        if (operator.isOutput()) {
            println("C")

            return when (operator) {
                GET -> panel.pageIndex
                GET_MAX -> panel.maxPageIndex
                else -> error("unreachable")
            }.let { CompletableFuture.completedFuture(it) }
        }

        // 修改 Page
        if (value != null) {
            frame.run(value).int { operator.run(panel, it) }
        } else {
            operator.run(panel, 1)
        }

        return CompletableFuture.completedFuture(panel.pageIndex)
    }

    companion object {

        /*
        page
        page max
        page next [value]
        page previous [value]
        page set <value>
         */

        @KetherParser(["page"], namespace = "invero", shared = true)
        fun parser() = parser(null)

        internal fun parser(indexs: List<Int>? = null) = scriptParser {
            val method = if (it.hasNext()) {
                when (it.nextToken()) {
                    "max" -> GET_MAX
                    "to", "set", "=" -> MODIFY
                    "previous", "prev", "-" -> PREVIOUS
                    "next", "+" -> NEXT
                    else -> GET
                }
            } else GET

            val value = if (!method.isOutput() && it.hasNext()) it.nextParsedAction() else null

            ActionPage(method, value, indexs).also {
                println("Parsed: $it")
            }
        }

    }

}