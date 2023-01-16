package cc.trixey.invero.core.icon

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
class IconElement(panel: Panel) : SimpleItem(panel) {

    // 物品帧
    var currentFrame: Frame? = null

    // 子图标指向
    var subIconIndex: Int = -1

    // 动态帧属性
    var framesDefaultDelay: Long? = null
    var framesCyclic: Cyclic<Frame>? = null
        set(value) {
            field = value
            if (value != null && !value.isSingle()) {
                runFrameTask()
            }
        }

    private var frameTaskRunning = false

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
        }
    }

}