package cc.trixey.invero.core.script.js

import cc.trixey.invero.core.script.session
import org.bukkit.entity.Player
import taboolib.common.platform.function.server
import taboolib.common5.compileJS
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.SimpleBindings
import javax.script.SimpleScriptContext

/**
 * Invero
 * cc.trixey.invero.core.script.js.JavaScriptAgent
 *
 * @author Arasple
 * @since 2023/2/7 21:13
 */
object JavaScriptAgent {

    private val bindings = mapOf(
        "server" to server(),
        "Utils" to Helper()
    )

    @KetherParser(["$", "js", "javascript"], namespace = "invero", shared = true)
    fun parser() = scriptParser {
        ActionJavaScript(it.nextToken().trimIndent().compileJS()!!)
    }

    /**
     * @author IzzelAliz
     */
    class ActionJavaScript(val script: CompiledScript) : ScriptAction<Any>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Any> {
            val jsContext = SimpleScriptContext()
            val session = frame.session()

            jsContext.setBindings(SimpleBindings(bindings).apply {
                this["session"] = session
                this["player"] = session?.viewer?.get<Player>()
                this["sender"] = this["player"]

                putAll(frame.deepVars().filterNot { it.key.startsWith("@") })
            }, ScriptContext.ENGINE_SCOPE)

            jsContext.setAttribute("parse", Function<Any, Any> {
                session?.parse(it.toString())
            }, ScriptContext.ENGINE_SCOPE)
            return CompletableFuture.completedFuture(script.eval(jsContext))
        }

    }


}