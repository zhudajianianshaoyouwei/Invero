package cc.trixey.invero.core.script

import cc.trixey.invero.core.script.loader.InveroKetherParser
import cc.trixey.invero.ui.common.Panel
import taboolib.common5.cint
import taboolib.module.kether.*

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionPanel
 *
 * @author Arasple
 * @since 2023/1/19 21:20
 */
object ActionPanel {

    /*
    panel {at 0 at 1 at 2} <handler>

    handlers:
    - page
    - scroll
    - shift
    - filter
    - icon
     */
    @InveroKetherParser(["panel"])
    fun parser() = scriptParser {
        val indexs = mutableListOf<Int>()

        actionNow {
            while (it.hasNext()) {
                when (it.expects("at", "page", "icon")) {
                    "at" -> indexs += newFrame(it.nextParsedAction()).run<Any>().getNow(null).cint
                    "page" -> {
                        ActionPage.parserPage(locatePanel(indexs)).reader.invoke(it)
                        break
                    }

                    "scroll" -> {
                        ActionScroll.parserScroll(locatePanel(indexs))
                        break
                    }

                    "icon" -> {
                        ActionIcon.parser(locatePanel(indexs)).reader.invoke(it)
                        break
                    }
                }
            }
        }
    }

    private fun <T : Panel> ScriptFrame.locatePanel(indexs: List<Int>): T? {
        return if (indexs.isEmpty()) null else findPanelAt(indexs)
    }

}