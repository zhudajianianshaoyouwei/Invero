package cc.trixey.invero.core.kether

import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.deepVars

/**
 * Invero
 * cc.trixey.invero.core.kether.ActionValidation
 *
 * @author Arasple
 * @since 2023/2/2 19:55
 */
object ActionValidation {

    @KetherParser(["var"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(
            symbol(),
            symbol()
        ).apply(it) { name, check ->
            now {
                when (check) {
                    "has", "contains" -> name in deepVars()
                    "valid" -> !variables().getOrNull<Any?>(name)?.toString().isNullOrBlank()
                    else -> println("unknown check type: $check")
                }
            }
        }
    }

}