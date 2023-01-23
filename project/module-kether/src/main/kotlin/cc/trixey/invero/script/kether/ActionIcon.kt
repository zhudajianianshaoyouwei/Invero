package cc.trixey.invero.script.kether

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

    pause_update
    pause_relocate
    pause_frames
    resume_update
    resume_relocate
    resume_frames
     */

    @KetherParser(["icon"], namespace = "invero", shared = true)
    fun parser() = combinationParser {
        it.group(
            command("by", then = text()).option().defaultsTo(null),
            command("at", then = int()).option().defaultsTo(-1),
            symbol(),
        ).apply(it) { by, at, action ->
            now {
                when (action) {
                    "relocate" -> {
                        iconElementBy(by, at).relocate()
                    }

                    "update" -> {
                        iconElementBy(by, at).update()
                    }

                    "item" -> {
                        iconElementBy(by, at).value
                    }

                    "pause_update" -> {
                        iconElementBy(by, at).pauseUpdateTask()
                    }

                    "pause_relocate" -> {
                        iconElementBy(by, at).pauseRelocateTask()
                    }

                    "pause_frames" -> {
                        iconElementBy(by, at).pauseFramesTask()
                    }

                    "resume_update" -> {
                        iconElementBy(by, at).resumeUpdateTask()
                    }

                    "resume_relocate" -> {
                        iconElementBy(by, at).resumeRelocateTask()
                    }

                    "resume_frames" -> {
                        iconElementBy(by, at).resumeFramesTask()
                    }

                    else -> error("Unsupported action for icon: $action")
                }
            }
        }
    }

}