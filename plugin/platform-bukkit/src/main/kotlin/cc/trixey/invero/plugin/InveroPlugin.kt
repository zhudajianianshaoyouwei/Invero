package cc.trixey.invero.plugin

import cc.trixey.invero.common.adventure.parseMiniMessage
import cc.trixey.invero.common.adventure.translateAmpersandColor
import cc.trixey.invero.core.util.KetherHandler
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

/**
 * Invero
 * cc.trixey.invero.plugin.InveroPlugin
 *
 * @author Arasple
 * @since 2022/12/20 20:42
 */
object InveroPlugin : Plugin() {

    override fun onLoad() {
        info("Loaded")

        repeat(1000) {
            KetherHandler
                .parseInline("<red><gradient>{{text}}", null, emptyMap())
                .parseMiniMessage()
                .translateAmpersandColor()
        }
    }

}