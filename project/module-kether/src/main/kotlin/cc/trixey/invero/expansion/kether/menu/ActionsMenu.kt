package cc.trixey.invero.expansion.kether.menu

import cc.trixey.invero.core.InveroManager
import cc.trixey.invero.expansion.kether.getPlayer
import cc.trixey.invero.expansion.kether.getSession
import taboolib.module.kether.*

/**
 * Invero
 * cc.trixey.invero.expansion.kether.menu.ActionsMenu
 *
 * @author Arasple
 * @since 2023/1/19 17:37
 */

/*
sender::PLAYER

- menu title set "xxx"
- menu title pause
- menu title resume
- menu close
- menu open [menuId]
 */


@KetherParser(["menu"], namespace = "invero", shared = true)
fun parserMenu() = scriptParser {

    when (it.expects("title", "close", "open")) {

        "title" -> {
            when (it.expects("set", "pause", "resume")) {

                "set" -> {
                    val input = it.nextParsedAction()
                    actionFuture { future ->
                        val session = getSession()
                        newFrame(input).run<String>().thenApply { title ->
                            session.viewingWindow?.title = session.parse(title)
                            future.complete(title)
                        }
                    }
                }

                "pause" -> {
                    actionNow {
                        getSession().taskTitleFrame = false
                        false
                    }
                }

                "resume" -> {
                    actionNow {
                        getSession().taskTitleFrame = true
                        true
                    }
                }

                else -> error("Unknown case")
            }
        }

        "close" -> {
            actionNow { getSession().closeMenu() }
        }

        "open" -> {
            val input = it.nextParsedAction()
            actionFuture { future ->
                newFrame(input).run<String>().thenApply { id ->
                    val menu = InveroManager.getMenu(id)
                    if (menu == null) error("Unknown menu id: $id")
                    else menu.open(getPlayer())
                    future.complete(id)
                }
            }
        }

        else -> error("Unknown case")
    }

}