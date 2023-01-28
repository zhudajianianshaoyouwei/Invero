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
            // get, has, set, del, inc, dec
            symbol(),
            // key
            text().option(),
            // value
            command("to", "by", then = action()).option().defaultsTo(null)
        ).apply(it) { action, key, mod ->

            future {
                val value = if (mod != null) newFrame(mod).run<Any>() else null
                if (key == null || action == "update") {
                    return@future completedFuture(session()?.updateVariables())
                }

                when (action) {
                    "get" -> completedFuture(session()?.getVariable(key))
                    "has" -> completedFuture(session()?.hasVariable(key) ?: false)
                    "set" -> {
                        (value ?: error("No valid value")).thenApply { session()?.setVariable(key, it) }
                    }

                    "rem", "del", "delete" -> {
                        completedFuture(session()?.removeVariable(key))
                    }

                    "inc", "increase", "+=" -> {
                        (value ?: error("No valid value")).thenApply {
                            session()?.apply {
                                setVariable(key, getVariable(key).cdouble + it.cdouble)
                            }
                        }
                    }

                    "dec", "decrease", "-=" -> {
                        (value ?: error("No valid value")).thenApply {
                            session()?.apply {
                                setVariable(key, getVariable(key).cdouble - it.cdouble)
                            }
                        }
                    }

                    else -> error("Unknown action $action")
                }
            }
        }
    }

}