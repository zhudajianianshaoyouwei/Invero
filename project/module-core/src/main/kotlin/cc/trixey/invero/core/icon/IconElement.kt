package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.bukkit.util.CoroutineTask
import cc.trixey.invero.bukkit.util.launchAsync
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.util.context
import cc.trixey.invero.core.util.letCatching

/**
 * Invero
 * cc.trixey.invero.core.icon.IconElement
 *
 * @author Arasple
 * @since 2023/1/16 12:22
 */
open class IconElement(val session: Session, val icon: Icon, val agent: AgentPanel, panel: Panel) : SimpleItem(panel) {

    /*
    本图标元素的任务状态
    配合 Kether 动作实现暂停周期的功能
     */
    val taskStatus = arrayOf(
        // 翻译物品变量 (Update)
        true,
        // 重定向子图标 (Relocate)
        true,
        // 动态帧播放 (Frames)
        true
    )

    /*
    图标指向

    <0 默认主图标
    >=0 子图标的数组坐标
     */
    var iconIndex: Int = -1

    /*
    当前的有效的物品帧
     */
    var frame: Frame? = null
        set(value) {
            value?.render(session, agent, this)
            field = value
        }

    /*
    当前的有效主物品帧
     */
    var framesDefaultDelay: Long = icon.framesProperties?.defaultDelay ?: 20

    /*
    默认的物品帧
     */
    val defaultFrame: Frame = icon.defaultFrame!!

    /*
    当前正在进行循环动画的物品帧集合
     */
    var framesCyclic: Cyclic<Frame>? = null
        set(value) {
            field = value
            if (value != null && !value.isSingle()) {
                submitFrameTask()
            }
        }

    fun invoke() {
        // 默认帧相关
        defaultFrame.render(session, agent, this)
        icon.getValidId(agent)
            ?.let { key -> agent.layout?.search(key) }
            ?.let { this@IconElement.set(it) }

        framesCyclic = icon.generateCyclicFrames()

        // 周期任务 :: 翻译物品帧的相关变量
        if (icon.updatePeriod > 0) {
            session.launchAsync(delay = 20L, period = icon.updatePeriod) {
                if (taskStatus[0]) {
                    frame?.translateUpdate(session, this, defaultFrame)
                }
            }
        }

        // 周期任务 :: 重定向子图标
        if (icon.relocatePeriod > 0 && !icon.subIcons.isNullOrEmpty()) {
            session.launchAsync(delay = 20L, period = icon.relocatePeriod) {
                if (taskStatus[1]) {
                    val previousIndex = iconIndex
                    val relocatedIndex = icon.subIcons.indexOfFirst { it.condition?.evalInstant(context()) ?: false }
                    // 子图标 ->> 默认图标
                    if (previousIndex > 0 && relocatedIndex < 0) {
                        framesDefaultDelay = icon.framesProperties?.defaultDelay ?: 20L
                        framesCyclic = icon.generateCyclicFrames()
                    } else if (previousIndex != relocatedIndex) {
                        val subIcon = icon.subIcons[relocatedIndex]
                        iconIndex = relocatedIndex
                        framesCyclic = subIcon.generateCyclicFrames()
                        framesDefaultDelay = subIcon.framesProperties?.defaultDelay ?: 20L
                    }
                }
            }
        }

        // 交互逻辑
        onClick { _ ->
            getIconHandler()?.letCatching {
                it.all?.run(context())?.get()
                it.response[clickType]?.run(context())?.get()
            }
            true
        }
    }

    private var frameTask: CoroutineTask? = null

    fun submitFrameTask() {
        if (frameTask != null) frameTask?.cancel()

        frameTask = launchAsync {
            loop@ while (true) {
                if (taskStatus[2]) {
                    val frames = framesCyclic ?: break@loop
                    if (frames.isAnimationEnded()) taskStatus[2] = false
                    else frames.getAndCycle().let {
                        this@IconElement.frame = it
                        wait(frame?.delay ?: framesDefaultDelay)
                    }
                }
            }
        }.also { session.taskManager += it }
    }

    fun getIconHandler(): IconHandler? {
        return if (iconIndex > 0) icon.subIcons!![iconIndex].handler ?: icon.handler else icon.handler
    }

}