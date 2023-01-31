package cc.trixey.invero.script.kether

import cc.trixey.invero.common.panel.ElementalPanel
import taboolib.common.platform.function.submitAsync
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Invero
 * cc.trixey.invero.script.kether.ActionIcon
 *
 * @author Arasple
 * @since 2023/1/21 19:47
 */
object ActionIcon {

    /*
    icon [by <id>] [at <slot>] update/relocate/item

    refresh
    pause_update
    pause_relocate
    pause_frames
    resume_update
    resume_relocate
    resume_frames
     */

    @KetherParser(["icon"], namespace = "invero", shared = true)
    fun parser() = parser(null)

    fun parser(ref: ElementalPanel? = null) = combinationParser {
        it.group(
            command("by", then = text()).option().defaultsTo(null),
            command("at", then = int()).option().defaultsTo(-1),
            symbol(),
        ).apply(it) { by, at, action ->
            now {
                iconElementBy(by, at, ref).apply {
                    if (action == "item") return@now value
                    else submitAsync(delay = 2L) {
                        when (action) {
                            "relocate" -> relocate()
                            "update" -> update()
                            "refresh" -> {
                                relocate()
                                update()
                            }

                            "pause_update" -> pauseUpdateTask()
                            "pause_relocate" -> pauseRelocateTask()
                            "pause_frames" -> pauseFramesTask()
                            "resume_update" -> resumeUpdateTask()
                            "resume_relocate" -> resumeRelocateTask()
                            "resume_frames" -> resumeFramesTask()
                            else -> error("Unsupported action for icon: $action")
                        }
                    }
                }
            }
        }
    }

}