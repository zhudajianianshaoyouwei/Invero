package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.script.selfSourceObject
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionSource
 *
 * @author Arasple
 * @since 2023/1/29 22:47
 */
object ActionSource {

    @KetherParser(["element"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(text()).apply(it) { key ->
            now { selfSourceObject()[key] }
        }
    }

}