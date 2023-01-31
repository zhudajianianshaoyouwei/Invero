package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.bukkit.element.item.SimpleItem
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Context
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.item.Frame
import cc.trixey.invero.core.util.session
import taboolib.common.platform.function.submitAsync

/**
 * Invero
 * cc.trixey.invero.core.icon.IconElement
 *
 * @author Arasple
 * @since 2023/1/16 12:22
 */
open class IconElement(
    val session: Session,
    val icon: Icon,
    val agent: AgentPanel,
    panel: Panel,
    vars: Map<String, Any>
) : SimpleItem(panel) {

    private val paused = arrayOf(true, true, true)

    internal val context by lazy { Context(session.viewer, session, panel, this, vars) }

    private var iconIndex = relocateIndex()

    private var frame: Frame? = null
        set(value) {
            value?.render(session, agent, this)
            field = value
        }

    private var framesDefaultDelay: Long = icon.framesProperties?.defaultDelay ?: 20

    private var framesCyclic: Cyclic<Frame>? = null
        set(value) {
            val previousIndex = field?.index
            field = value
            if (value != null && !value.isSingle()) submitFrameTask(previousIndex)
        }

    fun invoke() {
        // 默认帧
        frame = icon.defaultFrame
        // 布局定位
        agent.layout
            ?.search(icon.id)
            ?.let { this@IconElement.set(it) }
        // 循环物品帧
        framesCyclic = icon.generateCyclicFrames()
        // 周期任务：翻译物品帧的相关变量
        if (icon.periodUpdate > 0) {
            session.taskMgr.launchAsync(delay = 10L, period = icon.periodUpdate) {
                if (isVisible() && paused[0]) update()
            }
        }
        // 周期任务：重定向子图标
        if (icon.periodRelocate > 0 && !icon.subIcons.isNullOrEmpty()) {
            session.taskMgr.launchAsync(delay = 10L, period = icon.periodRelocate) {
                if (isVisible() && paused[1]) relocate()
            }
        }
        // 交互逻辑
        onClick { type, _ -> getIconHandler()?.run(context, type) }
    }

    /**
     * 提交动画循环任务
     */
    private fun submitFrameTask(inherit: Int?) {
        val frames = framesCyclic!!
        if (inherit != null && inherit <= frames.maxIndex) frames.index = inherit

        fun loop(delay: Long) {
            submitAsync(delay = delay) {
                if (frames != framesCyclic || frames.isAnimationEnded() || shouldUnregister()) return@submitAsync
                if (isVisible() && paused[2]) {
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
        if (icon.subIcons.isNullOrEmpty()) return

        val previousIndex = iconIndex
        val relocatedIndex = relocateIndex()

        if (previousIndex >= 0 && relocatedIndex < 0) {
            iconIndex = -1
            frame = icon.defaultFrame
            framesDefaultDelay = icon.framesProperties?.defaultDelay ?: 20L
            framesCyclic = icon.generateCyclicFrames()
        } else if (relocatedIndex >= 0 && relocatedIndex != previousIndex) {
            val subIcon = icon.subIcons[relocatedIndex]
            iconIndex = relocatedIndex
            frame = subIcon.defaultFrame
            framesCyclic = subIcon.generateCyclicFrames()
            framesDefaultDelay = subIcon.framesProperties?.defaultDelay ?: 20L
        }
    }

    fun relocateIndex(): Int {
        return icon.subIcons?.indexOfFirst { it.condition?.evalInstant(context) ?: false } ?: -1
    }

    fun pauseUpdateTask() {
        paused[0] = false
    }

    fun pauseRelocateTask() {
        paused[1] = false
    }

    fun pauseFramesTask() {
        paused[2] = false
    }

    fun resumeUpdateTask() {
        paused[0] = true
    }

    fun resumeRelocateTask() {
        paused[1] = true
    }

    fun resumeFramesTask() {
        paused[2] = true
    }

    /**
     * 取得有效的交互处理器
     */
    fun getIconHandler(): IconHandler? {
        return (icon.subIcons?.getOrNull(iconIndex) ?: icon).handler
    }

    /**
     * 是否需要注销
     */
    fun shouldUnregister(): Boolean {
        return session != session.viewer.session
    }

}