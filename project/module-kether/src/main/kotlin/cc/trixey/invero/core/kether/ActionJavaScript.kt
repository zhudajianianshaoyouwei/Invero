package cc.trixey.invero.core.kether

import cc.trixey.invero.common.chemdah.InferItem.Companion.toInferItem
import cc.trixey.invero.common.util.letCatching
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.util.session
import cc.trixey.invero.ui.bukkit.util.fromURL
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.server
import taboolib.common5.compileJS
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.SimpleBindings
import javax.script.SimpleScriptContext

/**
 * @author IzzelAliz, Arasple
 */
class ActionJavaScript(val script: CompiledScript) : ScriptAction<Any>() {

    override fun run(frame: ScriptFrame): CompletableFuture<Any> {
        val ketherContext = frame.script()
        val result = letCatching {
            val context = SimpleScriptContext()
            val bindings = SimpleBindings(mapOf("sender" to ketherContext.sender, "server" to server()))
            bindings.putAll(frame.deepVars())
            val event = Event(bindings, ketherContext, context).also { it.call() }

            event.scriptContext.let {
                it.setBindings(bindings, ScriptContext.ENGINE_SCOPE)
                script.eval(it)
            }
        }
        return CompletableFuture.completedFuture(result)
    }

    class Event(
        val bindings: SimpleBindings,
        val ketherContext: taboolib.module.kether.ScriptContext,
        val scriptContext: ScriptContext
    ) : ProxyEvent() {

        override val allowCancelled: Boolean
            get() = false
    }

    companion object {

        @KetherParser(["$", "js", "javascript"], namespace = "invero", shared = true)
        fun parser() = scriptParser {
            ActionJavaScript(it.nextToken().trimIndent().compileJS()!!)
        }

        @SubscribeEvent
        fun e(e: Event) {
            val player = e.ketherContext.sender?.cast<Player>() ?: return
            val context = e.ketherContext.get<Context?>("@context")
            val session = player.session ?: return

            e.scriptContext.apply {
                // parse(String) -> String
                function("parse") { session.parse(it.toString(), context) }
                // inferItem(String) -> InferItem.Item
                function("inferItem") { it.toString().toInferItem() }
                // fromURL(link) -> String
                function("fromURL") { fromURL(it.toString()) }
                // fromURL(link) -> String
                function("jsonObject") { Json.decodeFromString(it.toString()) }
            }

            e.bindings["session"] = session
        }

        private fun ScriptContext.function(name: String, block: (Any) -> Any?) = setAttribute(
            name,
            java.util.function.Function<Any, Any?> { block(it) },
            ScriptContext.ENGINE_SCOPE
        )

    }
}