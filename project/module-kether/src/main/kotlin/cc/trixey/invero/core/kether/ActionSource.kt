package cc.trixey.invero.core.kether

import cc.trixey.invero.core.util.KetherHandler
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.core.kether.ActionSource
 *
 * @author Arasple
 * @since 2023/1/29 22:47
 */
object ActionSource {

    @KetherParser(["element"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(text()).apply(it) { key ->
            now {
                val value = selfSourceObject()[key]
                if (value?.startsWith("ext@") == true) {
                    val source = value.removePrefix("ext@")
                    KetherHandler.invoke(source, player(), variables().toMap()).getNow("<TIMEOUT>").toString()
                } else {
                    value
                }
            }
        }
    }

}