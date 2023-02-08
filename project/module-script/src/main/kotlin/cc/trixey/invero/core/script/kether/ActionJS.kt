package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.script.session
import cc.trixey.invero.core.util.runJS
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.deepVars

/**
 * Invero
 * cc.trixey.invero.core.script.kether.ActionJS
 *
 * @author Arasple
 * @since 2023/2/8 11:09
 */
object ActionJS {

    @KetherParser(["$", "js", "javascript"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(symbol()).apply(it) { script ->
            future { runJS(script, session(), deepVars().filterNot { v -> v.key.startsWith("@") }) }
        }
    }

}