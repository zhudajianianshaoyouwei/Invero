package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.util.letCatching
import cc.trixey.invero.core.util.session
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.core.icon.IconElement
 *
 * @author Arasple
 * @since 2023/1/16 12:22
 */
open class IconElement(val session: Session, val icon: Icon, val agent: AgentPanel, panel: Panel) : SimpleItem(panel) {

    // 任务是否未被暂停
    private val taskStatus = arrayOf(
        // 翻译物品变量 (Update)
        true,
        // 重定向子图标 (Relocate)
        true,
        // 动态帧播放 (Frames)
        true
    )

    // 针对本图标的上下文
    val context by lazy {
        Context(session.viewer, session, panel, this)
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

    // 正在循环的集合
    private var framesCyclic: Cyclic<Frame>? = null
        set(value) {
            field = value
            if (value != null && !value.isSingle()) {
                submitFrameTask()
            }
        }

    /**
     * 部署此图标的相关任务
     */
    fun invoke() {
        // 默认帧相关
        frame = icon.defaultFrame
        icon.getValidId(agent)?.let { key -> agent.layout?.search(key) }?.let { this@IconElement.set(it) }

        framesCyclic = icon.generateCyclicFrames()

        // 周期任务 :: 翻译物品帧的相关变量
        if (icon.periodUpdate > 0) {
            session.taskMgr.launchAsync(delay = 10L, period = icon.periodUpdate) {
                if (isVisible() && taskStatus[0]) update()
            }
        }

        // 周期任务 :: 重定向子图标
        if (icon.periodRelocate > 0 && !icon.subIcons.isNullOrEmpty()) {
            session.taskMgr.launchAsync(delay = 10L, period = icon.periodRelocate) {
                if (isVisible() && taskStatus[1]) relocate()
            }
        }

        // 交互逻辑
        onClick { clickType, _ ->
            getIconHandler()?.letCatching {
                submitAsync {
                    it.all?.run(context)?.get()
                    it.response[clickType]?.run(context)?.get()
                }
            }
        }
    }

    /**
     * 提交动画循环任务
     */
    private fun submitFrameTask() {
        val frames = framesCyclic!!

        fun loop(delay: Long) {
            submitAsync(delay = delay) {
                if (frames != framesCyclic || frames.isAnimationEnded() || shouldUnregister()) return@submitAsync
                if (isVisible() && taskStatus[2]) {
                    frame = frames.getAndCycle()
                    loop(frame?.delay ?: framesDefaultDelay)
                }
            }.also { session.taskMgr += it }
        }

        loop(0)
    }

    /**
     * 翻译当前物品帧的变量
     */
    fun update() {
        frame?.translateUpdate(session, this, icon.defaultFrame)
    }

    /**
     * 重新定位子图标
     */
    fun relocate() {
        if (icon.subIcons == null) return

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

    fun pauseUpdateTask() {
        taskStatus[0] = false
    }

    fun pauseRelocateTask() {
        taskStatus[1] = false
    }

    fun pauseFramesTask() {
        taskStatus[2] = false
    }

    fun resumeUpdateTask() {
        taskStatus[0] = true
    }

    fun resumeRelocateTask() {
        taskStatus[1] = true
    }

    fun resumeFramesTask() {
        taskStatus[2] = true
    }

    /**
     * 取得有效的交互处理器
     */
    fun getIconHandler(): IconHandler? {
        return if (iconIndex > 0) icon.subIcons!![iconIndex].handler ?: icon.handler else icon.handler
    }

    /**
     * 是否需要注销
     */
    fun shouldUnregister(): Boolean {
        return session != session.viewer.session
    }

}