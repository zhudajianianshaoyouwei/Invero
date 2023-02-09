package cc.trixey.invero.core.api

import cc.trixey.invero.common.Invero
import cc.trixey.invero.common.api.JavaScriptHandler
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.server
import taboolib.common5.compileJS
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.SimpleBindings
import javax.script.SimpleScriptContext

/**
 * Invero
 * cc.trixey.invero.core.api.DefaultJavaScriptHandler
 *
 * @author Arasple
 * @since 2023/2/8 10:57
 */
class DefaultJavaScriptHandler : JavaScriptHandler {

    private val cacheCompiled = ConcurrentHashMap<String, CompiledScript>()
    private val persistBindings = ConcurrentHashMap<String, Any>(
        buildMap {
            put("server", server())
            put("Utils", Helper())
        }
    )
    private val persistFunctions = ConcurrentHashMap<String, Function<Any, Any>>()

    override fun runScript(
        script: String,
        cache: Boolean,
        bindings: Map<String, Any?>,
        block: SimpleScriptContext.() -> Unit
    ): Any? {
        val context = SimpleScriptContext()
        context.setBindings(
            SimpleBindings(persistBindings).also { it += bindings },
            ScriptContext.ENGINE_SCOPE
        )
        persistFunctions.forEach { (key, value) -> context.setAttribute(key, value, ScriptContext.ENGINE_SCOPE) }

        block(context)

        val compiledScript =
            if (cache) cacheCompiled.computeIfAbsent(script) { it.compileJS()!! }
            else script.compileJS()!!

        return compiledScript.eval(context)
    }

    override fun registerPersistFunction(name: String, function: Function<Any, Any>) {
        persistFunctions[name] = function
    }

    override fun registerPersistBindings(name: String, bindings: Map<String, Any>) {
        persistBindings += bindings
    }

    /**
     * 默认的内置辅助工具类
     */
    class Helper {

        private val serializer: Json
            get() = Invero
                .API
                .getMenuManager()
                .getJsonSerializer<Json>()

        /**
         * 读 URL 内容
         */
        fun fromURL(url: String) = try {
            String(URL(url).openStream().readBytes())
        } catch (t: Throwable) {
            "<ERROR: ${t.localizedMessage}>"
        }

        /**
         * JSON 处理
         */
        fun asJsonElemenet(json: String): JsonElement = serializer.decodeFromString(json)

        fun asJsonObject(json: String): JsonObject = serializer.decodeFromString(json)

        fun asJsonArray(json: String): JsonArray = serializer.decodeFromString(json)

    }

    companion object {

        @Awake(LifeCycle.INIT)
        fun init() {
            PlatformFactory.registerAPI<JavaScriptHandler>(DefaultJavaScriptHandler())
        }

    }

}