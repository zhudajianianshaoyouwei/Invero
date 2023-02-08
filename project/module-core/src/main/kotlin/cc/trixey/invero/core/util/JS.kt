package cc.trixey.invero.core.util

import cc.trixey.invero.common.Invero
import cc.trixey.invero.core.Session
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import javax.script.ScriptContext
import javax.script.SimpleScriptContext

/**
 * Invero
 * cc.trixey.invero.core.script.js.JS
 *
 * @author Arasple
 * @since 2023/2/8 11:52
 */
fun runJS(script: String, session: Session?, variables: Map<String, Any?> = emptyMap()): CompletableFuture<Any?> {
    val player = session?.viewer?.get<Player>()

    val bindings = buildMap {
        put("session", session)
        put("player", player)
        putAll(variables)
    }

    Invero.API.getJavaScriptHandler().runScript(script.trimIndent(), bindings = bindings) {
        registerFunction("parse") { param ->
            session?.parse(param.toString()) ?: "<ERROR: NO SESSION>"
        }
    }.let {
        return CompletableFuture.completedFuture(it)
    }
}

private fun SimpleScriptContext.registerFunction(name: String, block: (Any) -> Any) {
    setAttribute(name, Function<Any, Any> {
        block(it)
    }, ScriptContext.ENGINE_SCOPE)
}