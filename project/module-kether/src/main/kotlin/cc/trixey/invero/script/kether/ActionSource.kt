package cc.trixey.invero.script.kether

import cc.trixey.invero.core.util.KetherHandler
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.concurrent.CompletableFuture

/**
 * Invero
 * cc.trixey.invero.script.kether.ActionSource
 *
 * @author Arasple
 * @since 2023/1/29 22:47
 */
object ActionSource {

    @KetherParser(["source"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(text()).apply(it) { key ->
            future {
                val value = selfSourceObject()[key]
                if (value?.startsWith("source.extension:") == true) {
                    val source = value.removePrefix("source.extension:")
                    KetherHandler.invoke(source, player(), emptyMap())
                } else {
                    CompletableFuture.completedFuture(value)
                }
            }
        }
    }

}