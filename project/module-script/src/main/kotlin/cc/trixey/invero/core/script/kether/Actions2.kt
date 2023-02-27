package cc.trixey.invero.core.script.kether

import cc.trixey.invero.core.script.player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.core.script.kether.Actions2
 *
 * @author Arasple
 * @since 2023/2/26 18:01
 */
@KetherParser(["chance"], namespace = "invero", shared = true)
internal fun chance() = combinationParser {
    it.group(double()).apply(it) { random ->
        val chance = if (random > 1) random else random * 100.0

        now {
            return@now chance > (0..100).random()
        }
    }
}

@KetherParser(["playerPerform"], namespace = "invero", shared = true)
internal fun command() = combinationParser {
    it.group(action()).apply(it) { s ->
        now {
            newFrame(s).run<Any>().getNow(null)?.toString()?.let {
                player().performCommand(it)
            }
        }
    }
}