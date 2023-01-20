package cc.trixey.invero.script.kether

import cc.trixey.invero.common.panel.PagedPanel
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


/*
page get
page max
page next by [value]
page previous by [value]
page set <value>
 */
@KetherParser(["page"], namespace = "invero", shared = true)
fun parser() = parser(null)

fun parser(indexs: List<Int>?) = combinationParser {
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

        future { run(this, operator, value, indexs) }
    }
}


private fun run(
    frame: ScriptFrame,
    operator: PageOperator,
    value: ParsedAction<*>?,
    indexs: List<Int>?
): CompletableFuture<Any?> {
    val located = if (indexs.isNullOrEmpty()) null else frame.findPanelAt<PagedPanel>(indexs)
    val panel = located ?: frame.findNearstPanel() ?: return CompletableFuture.completedFuture(-1)

    // 输出 Page 值
    if (operator.isOutput()) {
        return when (operator) {
            PageOperator.GET -> panel.pageIndex
            PageOperator.GET_MAX -> panel.maxPageIndex
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