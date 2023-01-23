package cc.trixey.invero.script.kether

import taboolib.common5.cdouble
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.script.kether.ActionContext
 *
 * @author Arasple
 * @since 2023/1/23 11:02
 */
object ActionContext {

    @KetherParser(["context"], release = ["ctx"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(
            // action
            symbol(),
            // key
            text(),
            // value
            command("to", "by", then = action()).option().defaultsTo(null)
        ).apply(it) { action, key, value ->
            now {
                val modification = if (value != null) newFrame(value).run<Any>().get() else null

                when (action) {
                    "get" -> session()?.variables?.get(key)
                    "set" -> {
                        modification ?: error("Can not set nulled value as context variable")
                        session()?.variables?.set(key, modification)
                    }

                    "del", "rem", "delete" -> {
                        // TODO removable global, static
                        session()?.variables?.remove(key)
                    }

                    "add", "inc", "increase", "+=" -> {
                        val current =
                            session()?.variables?.get(key).toString().toDoubleOrNull() ?: error("Not a valid number")

                        session()?.variables?.set(key, current + modification.cdouble)
                    }

                    "sub", "dec", "decrease", "-=" -> {
                        val current =
                            session()?.variables?.get(key).toString().toDoubleOrNull() ?: error("Not a valid number")

                        session()?.variables?.set(key, current - modification.cdouble)
                    }

                    else -> error("Unknown operator $action")
                }
            }
        }
    }

}