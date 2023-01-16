package cc.trixey.invero.core.icon

import cc.trixey.invero.Session
import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.action.KetherCondition
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.animation.toCyclic
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.util.debug
import cc.trixey.invero.serialize.ListScoping
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import taboolib.common5.cbool

/**
 * Invero
 * cc.trixey.invero.core.icon.Icon
 *
 * @author Arasple
 * @since 2023/1/16 10:28
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
class Icon(
    // icon id
    @JsonNames("key")
    override val id: String? = null,
    // icon properties
    @JsonNames("require", "requirement", "rule")
    val condition: KetherCondition? = null,
    @SerialName("update")
    val updatePeriod: Long = -1,
    @SerialName("relocate") @JsonNames("relocate", "refresh")
    val relocatePeriod: Long = -1,
    // animation support
    @JsonNames("frames-properties", "frames-prop")
    val framesProperties: Frame.Properties? = null,
    @JsonNames("frame", "animation")
    val frames: List<Frame>? = null,
    // default frame
    @Serializable @JsonNames("material", "mat")
    val texture: Texture? = null,
    val name: String? = null,
    @Serializable(with = ListScoping::class)
    @JsonNames("lores")
    val lore: List<String>? = null,
    @Serializable @JsonNames("amt")
    val amount: Int? = null,
    @Serializable(with = ListScoping::class) @JsonNames("slots", "pos", "position", "positions")
    val slot: List<Slot>? = null,
    // parent, sub icons
    @Transient
    var parent: AgentIcon? = null,
    @Serializable(with = ListScoping::class) @SerialName("sub")
    val subIcons: List<Icon>? = null,
) : AgentIcon() {

    @Transient
    private val defaultFrame = Frame(null, texture, name, lore, amount, slot)


    init {
        subIcons?.forEach { it.parent = this }

        require(arrayOf(texture, frames).any { it != null }) {
            "Valid texture(material) for this icon is required"
        }
    }

    override fun invoke(session: Session, agent: AgentPanel, panel: Panel): IconElement {

        // 创建 IconElement 对象并且渲染默认帧
        val element = IconElement(session, panel)
        defaultFrame.render(session, agent, element)

        // 应用 Layout 定义的槽位
        getValidId(agent)
            ?.let { key -> agent.layout?.search(key) }
            ?.let { element.set(it) }

        // 部署多帧图标
        element.onFrameChange { it.render(session, agent, element) }
        element.framesDefaultDelay = framesProperties?.defaultDelay
        element.framesCyclic = getCyclicFrames()

        // 周期任务 :: 翻译物品帧的相关变量
        if (updatePeriod > 0) {
            debug("UPDATE TASK: $updatePeriod")
            session.launchAsync(delay = 20L, period = updatePeriod) {
                element.currentFrame?.translateUpdate(session, element, defaultFrame)
            }
        }

        // 周期任务 :: 重定向子图标
        if (relocatePeriod > 0 && !subIcons.isNullOrEmpty())
            session.launchAsync(delay = 20L, period = relocatePeriod) {
                val previousIndex = element.subIconIndex
                val relocatedIndex = subIcons
                    .indexOfFirst { it.condition?.eval(session)?.getNow(false).cbool }

                if (previousIndex > 0 && relocatedIndex < 0) {
                    element.framesDefaultDelay = framesProperties?.defaultDelay
                    element.framesCyclic = getCyclicFrames()
                } else if (previousIndex != relocatedIndex) {
                    val subIcon = subIcons[relocatedIndex]
                    element.subIconIndex = relocatedIndex
                    element.framesCyclic = subIcon.getCyclicFrames()
                    element.framesDefaultDelay = subIcon.framesProperties?.defaultDelay
                }
            }

        return element
    }

    fun getProperIcon(element: IconElement): Icon {
        return if (element.subIconIndex < 0 || subIcons.isNullOrEmpty()) this
        else subIcons[element.subIconIndex]
    }

    fun getCyclicFrames(): Cyclic<Frame>? {
        return frames?.toCyclic(framesProperties?.frameMode ?: CycleMode.LOOP)
    }

}