package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.bukkit.util.CoroutineTask
import cc.trixey.invero.bukkit.util.launchAsync
import cc.trixey.invero.common.Panel
import cc.trixey.invero.common.Pos
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.util.letCatching

/**
 * Invero
 * cc.trixey.invero.core.icon.IconElement
 *
 * @author Arasple
 * @since 2023/1/16 12:22
 */
open class IconElement(val session: Session, val icon: Icon, val agent: AgentPanel, panel: Panel) : SimpleItem(panel) {

    // 任务是否未被暂停
    val taskStatus = arrayOf(
        // 翻译物品变量 (Update)
        true,
        // 重定向子图标 (Relocate)
        true,
        // 动态帧播放 (Frames)
        true
    )

    // 针对本图标的上下文
    val context by lazy {
        Context(session, panel, this)
    }

    // 子图标定位
    private var iconIndex: Int = -1

    // 当前使用的物品帧属性
    private var frame: Frame? = null
        set(value) {
            value?.render(session, agent, this)
            field = value
        }

    // 多帧物品的默认持续时间
    private var framesDefaultDelay: Long = icon.framesProperties?.defaultDelay ?: 20

    // 默认物品帧
    private val defaultFrame: Frame = icon.defaultFrame!!

    // 正在循环的集合
    private var framesCyclic: Cyclic<Frame>? = null
        set(value) {
            field = value
            if (value != null && !value.isSingle()) {
                submitFrameTask()
            }
        }

    // 正在播放动画的任务
    private var frameTask: CoroutineTask? = null

    /**
     * 部署此图标的相关任务
     */
    fun invoke() {
        // 默认帧相关
        frame = defaultFrame
        icon.getValidId(agent)
            ?.let { key -> agent.layout?.search(key) }
            ?.let { this@IconElement.set(it) }

        framesCyclic = icon.generateCyclicFrames()

        // 周期任务 :: 翻译物品帧的相关变量
        if (icon.updatePeriod > 0) {
            session.launchAsync(delay = 20L, period = icon.updatePeriod) {
                if (isVisible() && taskStatus[0]) {
                    frame?.translateUpdate(session, this, defaultFrame)
                }
            }
        }

        // 周期任务 :: 重定向子图标
        if (icon.relocatePeriod > 0 && !icon.subIcons.isNullOrEmpty()) {
            session.launchAsync(delay = 20L, period = icon.relocatePeriod) {
                if (isVisible() && taskStatus[1]) {
                    val previousIndex = iconIndex
                    val relocatedIndex = icon.subIcons.indexOfFirst { it.condition?.evalInstant(context) ?: false }
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
        onClick { clickType, _ ->
            getIconHandler()?.letCatching {
                it.all?.run(context)?.get()
                it.response[clickType]?.run(context)?.get()
            }
        }
    }

    /**
     * 提交动画循环任务
     */
    fun submitFrameTask() {
        if (frameTask != null) frameTask?.cancel()

        frameTask = launchAsync {
            loop@ while (true) {
                if (isVisible() && taskStatus[2]) {
                    val frames = framesCyclic ?: break@loop
                    if (frames.isAnimationEnded()) taskStatus[2] = false
                    else frames.getAndCycle().let {
                        this@IconElement.frame = it
                        wait(frame?.delay ?: framesDefaultDelay)
                    }
                }
            }
        }.also { session.registerTask(it) }
    }

    /**
     * 取得有效的交互处理器
     */
    fun getIconHandler(): IconHandler? {
        return if (iconIndex > 0) icon.subIcons!![iconIndex].handler ?: icon.handler else icon.handler
    }

}