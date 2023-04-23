package cc.trixey.invero.core.script.override

import cc.trixey.invero.core.script.loader.InveroKetherParser
import cc.trixey.invero.core.script.parse
import taboolib.common.platform.function.console
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

/**
 * @author IzzelAliz
 */
class ActionCommand(val command: ParsedAction<*>, private val type: Type) : ScriptAction<Void>() {

    enum class Type {

        PLAYER, OPERATOR, CONSOLE
    }

    override fun run(frame: ScriptFrame): CompletableFuture<Void> {
        return frame.run(command).thenAcceptAsync({

            // 虽有损耗，可以接受
            val command = it.toString().trimIndent().let { content ->
                frame.parse(content)
            }

            when (type) {
                Type.PLAYER -> {
                    val viewer = frame.player()
                    viewer.performCommand(command.replace("@sender", viewer.name))
                }

                Type.OPERATOR -> {
                    val viewer = frame.player()
                    val isOp = viewer.isOp
                    viewer.isOp = true
                    try {
                        viewer.performCommand(command.replace("@sender", viewer.name))
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }
                    viewer.isOp = isOp
                }

                Type.CONSOLE -> {
                    console().performCommand(command.replace("@sender", "console"))
                }
            }
        }, frame.context().executor)
    }

    object Parser {

        @InveroKetherParser(["command"])
        fun parser() = scriptParser {
            val command = it.nextParsedAction()
            it.mark()
            val by = try {
                it.expects("by", "with", "as")
                when (val type = it.nextToken().lowercase()) {
                    "player" -> Type.PLAYER
                    "op", "operator" -> Type.OPERATOR
                    "console", "server" -> Type.CONSOLE
                    else -> throw KetherError.NOT_COMMAND_SENDER.create(type)
                }
            } catch (ignored: Exception) {
                it.reset()
                Type.PLAYER
            }
            ActionCommand(command, by)
        }
    }

}