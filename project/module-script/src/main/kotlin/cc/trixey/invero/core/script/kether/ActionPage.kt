package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.script.findNearstPanelRecursively
import cc.trixey.invero.ui.common.panel.PagedPanel
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionPage
 *
 * @author Arasple
 * @since 2023/2/10 14:05
 */
object ActionPage {

    @KetherParser(["page"], namespace = "invero", shared = true)
    fun parserPage() = parserPage(null)

    internal fun parserPage(ref: PagedPanel?) = combinationParser {
        it.group(symbol()).apply(it) { type ->
            val operator = PageOperator.of(type)
            var value: ParsedAction<*>? = null
            if (operator == PageOperator.MODIFY) {
                it.group(action()).apply(it) { info -> value = info }
            } else if (!operator.isOutput()) {
                it.group(command("by", "to", then = action()).option().defaultsTo(null)).apply(it) { info ->
                    value = info
                }
            }
            future { runPage(this, operator, value, ref) }
        }
    }

    private fun runPage(
        frame: ScriptFrame,
        operator: PageOperator,
        value: ParsedAction<*>?,
        ref: PagedPanel?
    ): CompletableFuture<Any?> {
        val panel = ref ?: frame.findNearstPanelRecursively() ?: return CompletableFuture.completedFuture(-1)

        // 输出 Page 值
        if (operator.isOutput()) {
            return when (operator) {
                PageOperator.GET -> panel.pageIndex
                PageOperator.GET_MAX -> panel.maxPageIndex
                PageOperator.IS_FIRST_PAGE -> panel.pageIndex == 0
                PageOperator.IS_LAST_PAGE -> panel.pageIndex == panel.maxPageIndex
                else -> error("unreachable")
            }.let { CompletableFuture.completedFuture(it) }
        }

        // 修改 Page
        if (value != null) {
            frame.run(value).int { operator.invoke(panel, it) }
        } else {
            operator.invoke(panel, 1)
        }

        return CompletableFuture.completedFuture(panel.pageIndex)
    }

}