package cc.trixey.invero.core.icon

import cc.trixey.invero.Session
import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.bukkit.util.launchAsync
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.animation.Cyclic

/**
 * Invero
 * cc.trixey.invero.core.icon.IconElement
 *
 * @author Arasple
 * @since 2023/1/16 12:22
 */
class IconElement(val session: Session, panel: Panel) : SimpleItem(panel) {

    private var frameChangeCallback: (newFrame: Frame) -> Unit = {}
    private var frameTaskRunning = false

    var subIconIndex: Int = -1

    var framesDefaultDelay: Long? = null

    var currentFrame: Frame? = null
        set(value) {
            if (value != null) {
                frameChangeCallback(value)
            }
            field = value
        }

    var framesCyclic: Cyclic<Frame>? = null
        set(value) {
            field = value
            if (value != null && !value.isSingle()) {
                runFrameTask()
            }
        }

    fun onFrameChange(block: (newFrame: Frame) -> Unit) {
        frameChangeCallback = block
    }

    fun runFrameTask() {
        if (frameTaskRunning) return

        launchAsync {
            frameTaskRunning = true
            loop@ while (true) {
                val frame = framesCyclic?.getAndCycle() ?: break@loop
                val delay = frame.delay ?: framesDefaultDelay ?: 20L
                currentFrame = frame
                wait(delay)
            }
            frameTaskRunning = false
        }.let { session.taskManager += it }
    }

}