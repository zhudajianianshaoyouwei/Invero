package cc.trixey.invero.script.kether

import cc.trixey.invero.common.panel.ElementalPanel
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
                when (action) {
                    "relocate" -> {
                        iconElementBy(by, at, ref).relocate()
                    }

                    "update" -> {
                        iconElementBy(by, at, ref).update()
                    }

                    "refresh" -> {
                        iconElementBy(by, at, ref).apply {
                            relocate()
                            update()
                        }
                    }

                    "item" -> {
                        iconElementBy(by, at, ref).value
                    }

                    "pause_update" -> {
                        iconElementBy(by, at, ref).pauseUpdateTask()
                    }

                    "pause_relocate" -> {
                        iconElementBy(by, at, ref).pauseRelocateTask()
                    }

                    "pause_frames" -> {
                        iconElementBy(by, at, ref).pauseFramesTask()
                    }

                    "resume_update" -> {
                        iconElementBy(by, at, ref).resumeUpdateTask()
                    }

                    "resume_relocate" -> {
                        iconElementBy(by, at, ref).resumeRelocateTask()
                    }

                    "resume_frames" -> {
                        iconElementBy(by, at, ref).resumeFramesTask()
                    }

                    else -> error("Unsupported action for icon: $action")
                }
            }
        }
    }

}