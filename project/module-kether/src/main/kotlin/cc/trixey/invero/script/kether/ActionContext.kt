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

    @KetherParser(["context", "ctx"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(
            // get, set, del, inc, dec
            symbol(),
            // key
            text(),
            // value
            command("to", "by", then = action()).option().defaultsTo(null)
        ).apply(it) { action, key, mod ->

            future {
                val value = if (mod != null) newFrame(mod).run<Any>() else null

                when (action) {
                    "get" -> completedFuture(vars { get(key) })

                    "set" -> {
                        (value ?: error("No valid value")).thenApply { vars { put(key, it) } }
                    }

                    "rem", "del", "delete" -> {
                        completedFuture(vars { remove(key) })
                    }

                    "inc", "increase", "+=" -> {
                        (value ?: error("No valid value")).thenApply {
                            vars {
                                put(key, get(key).cdouble + it.cdouble)
                            }
                        }
                    }

                    "dec", "decrease", "-=" -> {
                        (value ?: error("No valid value")).thenApply {
                            vars { put(key, get(key).cdouble - it.cdouble) }
                        }
                    }

                    else -> error("Unknown action $action")
                }
            }
        }
    }

}