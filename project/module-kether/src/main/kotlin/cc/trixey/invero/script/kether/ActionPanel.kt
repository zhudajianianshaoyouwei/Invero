package cc.trixey.invero.script.kether

import taboolib.library.kether.QuestAction
import taboolib.library.kether.QuestReader
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
private fun handlePanelOperators(token: String, indexs: List<Int>, reader: QuestReader): QuestAction<*> {
    return when (token) {
//        "page" -> ActionPage.parser().let {
//            println(it.javaClass.simpleName)
//            it.reader.invoke(reader)
//        }

        else -> TODO()
    }
}

@KetherParser(["panel"], namespace = "invero", shared = true)
fun parsePanel() = scriptParser {
    val indexs = mutableListOf<Int>()

    actionNow {
        while (it.hasNext()) {
            when (it.expects("index", "at", "page", "scroll")) {
                "at", "index" -> indexs += it.nextInt()
                else -> {
                    handlePanelOperators(it.nextToken(), indexs, it)
                    break
                }
            }
        }
    }
}