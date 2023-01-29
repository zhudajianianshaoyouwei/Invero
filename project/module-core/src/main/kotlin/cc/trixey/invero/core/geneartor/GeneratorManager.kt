package cc.trixey.invero.core.geneartor

import cc.trixey.invero.core.geneartor.source.CustomGenerator
import cc.trixey.invero.core.geneartor.source.PlayerGenerator
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.concurrent.ConcurrentHashMap

/**
 * Invero
 * cc.trixey.invero.core.geneartor.GeneratorManager
 *
 * @author Arasple
 * @since 2023/1/29 21:58
 */
object GeneratorManager {

    private val sources = ConcurrentHashMap<String, () -> Generator>()

    @Awake(LifeCycle.ACTIVE)
    internal fun initDefaults() {
        register("custom") { CustomGenerator() }
        register("player") { PlayerGenerator() }
    }

    fun generateSourceBy(name: String): Generator {
        return sources.getOrElse(name) {
            sources.entries.find { it.key.equals(name, true) }?.value
        }?.invoke() ?: sources["custom"]!!.invoke()
    }

    fun register(name: String, block: () -> Generator) {
        sources[name] = block
    }

}