package cc.trixey.invero.core.script.kether

import cc.trixey.invero.common.supplier.Object
import cc.trixey.invero.core.script.contextVar
import cc.trixey.invero.core.script.findNearstPanel
import cc.trixey.invero.core.script.session
import cc.trixey.invero.core.util.KetherHandler
import cc.trixey.invero.ui.common.panel.GeneratorPanel
import cc.trixey.invero.ui.common.panel.PagedPanel
import org.bukkit.entity.Player
import taboolib.common5.Baffle
import taboolib.common5.cbool
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * Invero
 * cc.trixey.invero.expansion.script.menu.ActionPanelOperators
 *
 * @author Arasple
 * @since 2023/1/19 20:33
 */
object ActionPanelOperators {

    private val regeneratorBaffle by lazy {
        Baffle.of(500, TimeUnit.MILLISECONDS)
    }

    // regenerate filter <filter> sort <sortby>
    @KetherParser(["regenerate"], namespace = "invero", shared = true)
    fun parserGenerator() = combinationParser {
        it.group(
            command("filter", then = text()).option(),
            command("sort", then = text()).option()
        ).apply(it) { filter, sort ->
            now {
                val session = session() ?: return@now
                val viewer = session.viewer.get<Player>()
                if (!regeneratorBaffle.hasNext(viewer.name)) return@now
                val panel = findNearstPanel<GeneratorPanel<Object, *>>() ?: return@now
                val s = filter ?: contextVar<String>("@raw_filter")

                if (s != null) {
                    panel.filterBy { obj ->
                        KetherHandler
                            .invoke(s, viewer, session.getVariables(ext = obj.variables))
                            .getNow(true).cbool
                    }
                }
                if (sort != null) panel.sortWith { o1, o2 -> o1[sort]!!.compareTo(o2[sort]!!) }
                if (panel is PagedPanel) panel.pageIndex = 0

                panel.reset()
                panel.render()
            }
        }
    }

    /*
    page isFirst
    page isLast
    page get
    page max
    page next by [value]
    page previous by [value]
    page set <value]
     */
    @KetherParser(["page"], namespace = "invero", shared = true)
    fun parser() = parser(null)

    internal fun parser(ref: PagedPanel?) = combinationParser {
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
            future { run(this, operator, value, ref) }
        }
    }

    private fun run(
        frame: ScriptFrame,
        operator: PageOperator,
        value: ParsedAction<*>?,
        ref: PagedPanel?
    ): CompletableFuture<Any?> {
        val panel = ref ?: frame.findNearstPanel() ?: return CompletableFuture.completedFuture(-1)

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