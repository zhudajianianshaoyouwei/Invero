package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.script.findNearstPanelRecursively
import cc.trixey.invero.ui.common.panel.ScrollPanel
import taboolib.module.kether.KetherParser
import taboolib.module.kether.ParserHolder
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionScroll
 *
 * @author Arasple
 * @since 2023/2/10 14:06
 */
object ActionScroll {

    /*
    scroll index | get
    scroll next | right | down
    scroll previous | left | up
    scroll reset
     */
    @KetherParser(["scroll"], namespace = "invero", shared = true)
    fun parserScroll() = parserScroll(null)

    fun parserScroll(ref: ScrollPanel?) = combinationParser {
        it.group(symbol()).apply(it) { operator ->
            runScroll(ref, operator)
        }
    }

    fun ParserHolder.runScroll(panel: ScrollPanel?, operator: String) =
        now {
            val scroll = panel ?: findNearstPanelRecursively() ?: return@now "<PANEL NOT FOUND>"

            when (operator.lowercase()) {
                "index", "get" -> scroll.viewport.let { if (it.x > 0) it.x else it.y }
                "next", "right", "down" -> scroll.scroll(1)
                "previous", "left", "up" -> scroll.scroll(-1)
                "reset" -> scroll.resetViewport()
                else -> "<ERROR OPERATOR: $operator>"
            }
        }

}