package cc.trixey.invero.script.kether

import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.expects
import taboolib.module.kether.scriptParser

/**
 * Invero
 * cc.trixey.invero.script.kether.ActionPanel
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
    @KetherParser(["panel"], namespace = "invero", shared = true)
    fun parsePanel() = scriptParser {
        val indexs = mutableListOf<Int>()

        actionNow {
            while (it.hasNext()) {
                when (it.expects("at", "page", "icon")) {
                    "at" -> indexs += it.nextInt()
                    "page" -> {
                        ActionPage.parser(indexs).reader.invoke(it)
                        break
                    }

                    "icon" -> {
                        // by Name
                        it.expects("by", "at")
                        val iconId = it.nextToken()
                        break
                    }
                }
            }
        }
    }

    private fun handleIconOperators() {

    }

}