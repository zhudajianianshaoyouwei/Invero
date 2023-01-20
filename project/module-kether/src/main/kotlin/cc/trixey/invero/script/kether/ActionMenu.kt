package cc.trixey.invero.script.kether

import cc.trixey.invero.core.InveroManager
import taboolib.library.kether.QuestReader
import taboolib.module.kether.*
import taboolib.platform.util.onlinePlayers

/**
 * Invero
 * cc.trixey.invero.expansion.kether.menu.ActionsMenu
 *
 * @author Arasple
 * @since 2023/1/19 17:37
 */

/*
- menu title set "xxx"
- menu title pause
- menu title resume
- menu close
- menu open [menuId] for [player]
 */
@KetherParser(["menu"], namespace = "invero", shared = true)
fun parserMenu() = scriptParser {
    when (it.expects("title", "close", "open")) {
        "title" -> handlerMenuTitle(it)
        "close" -> actionNow { getSession().closeMenu() }
        "open" -> handlerMenuOpen(it)
        else -> error("Unknown case")
    }
}

private fun handlerMenuTitle(it: QuestReader) =
    when (it.expects("get", "set", "pause", "resume")) {
        "get" -> actionNow { getSession().window?.title }

        "set" -> {
            val input = it.nextParsedAction()
            actionFuture { future ->
                val session = getSession()
                newFrame(input).run<String>().thenApply { title ->
                    session.window?.title = session.parse(title)
                    future.complete(title)
                }
            }
        }

        "pause" -> {
            actionNow {
                getSession().variables["title_task_running"] = false
                false
            }
        }

        "resume" -> {
            actionNow {
                getSession().variables["title_task_running"] = true
                true
            }
        }

        else -> error("Unknown case")
    }


private fun handlerMenuOpen(reader: QuestReader): ScriptAction<Any?> {
    val input = reader.nextParsedAction()

    return actionFuture { future ->
        newFrame(input).run<Any>().thenApply {
            val id = it.toString()
            val menu = InveroManager.getMenu(id) ?: error("Not found menu with id $id")
            val player = if (reader.hasNext()) {
                reader.expect("for")
                reader.nextToken()
                reader.nextParsedAction()
            } else null

            if (player == null) {
                menu.open(getPlayer())
            } else {
                newFrame(player).run<Any>().thenApply { playerId ->
                    onlinePlayers
                        .find { p -> p.name == playerId }
                        ?.let { p -> menu.open(p) }
                }
            }
            future.complete(id)
        }
    }
}