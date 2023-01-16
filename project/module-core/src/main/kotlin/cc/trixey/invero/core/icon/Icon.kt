package cc.trixey.invero.core.icon

import cc.trixey.invero.bukkit.api.dsl.set
import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.action.KetherCondition
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.animation.toCyclic
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.panel.PanelStandard
import cc.trixey.invero.core.session.Session
import cc.trixey.invero.core.util.flatRelease
import cc.trixey.invero.serialize.ListScoping
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common5.cbool
import taboolib.platform.util.modifyMeta

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
    override val id: String? = null,
    @JsonNames("require", "requirement", "rule")
    val condition: KetherCondition? = null,
    @SerialName("frame-properties") @JsonNames("frames-properties")
    val framesProperties: FramesProperties? = null,
    @JsonNames("frame", "animation")
    val frames: List<Frame>? = null,
    @SerialName("update")
    val updatePeriod: Long = -1,
    @SerialName("relocate") @JsonNames("relocate", "refresh")
    val relocatePeriod: Long = -1,
    @Serializable
    val texture: Texture,
    val name: String? = null,
    @Serializable(with = ListScoping::class)
    val lore: List<String>? = null,
    @Serializable
    val amount: Int? = null,
    @Serializable(with = ListScoping::class)
    val slot: List<Slot>? = null,
    @Transient
    var parent: AgentIcon? = null,
    @Serializable(with = ListScoping::class) @SerialName("sub")
    val subIcons: List<Icon>? = null,
) : AgentIcon() {

    @Transient
    private val defaultFrame = Frame(null, texture, name, lore, amount, slot)

    init {
        subIcons?.forEach { it.parent = this }
    }

    override fun invoke(session: Session, agentPanel: AgentPanel, panel: Panel): IconElement {
        val element = IconElement(panel)

        // 渲染默认物品
        defaultFrame.render(session, agentPanel, element)
        element.framesDefaultDelay = framesProperties?.defaultDelay
        element.framesCyclic = getCyclicFrames()

        // 周期任务 :: 翻译物品帧的相关变量
        if (updatePeriod > 0) {
            session.launchAsync(delay = 20L, period = updatePeriod) {
                element.currentFrame?.render(session, agentPanel, element)
            }
        }
        // 周期任务 :: 重定向子图标
        if (relocatePeriod > 0 && !subIcons.isNullOrEmpty()) {
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
        }
        return element
    }

    fun Frame.render(session: Session, agentPanel: AgentPanel, element: IconElement, generateTexture: Boolean = true) {
        val item = if (texture != null && generateTexture) texture.generateItem(session).get() else element.value

        item.modifyMeta<ItemMeta> {
            if (name != null) setDisplayName(session.parse(name))
            if (this@render.lore != null) lore?.let {
                it.clear()
                it += session.parse(this@render.lore)
            }
        }

        if (amount != null) item.amount = amount
        if (slot != null) element.set(slot.flatRelease(agentPanel.scale))
        // else if element has no slots,
        //
        val defPos = getId(agentPanel)?.let { agentPanel.layout?.search(it) }

        element.value = item
    }

    fun getId(agentPanel: AgentPanel) = when {
        id != null -> id
        agentPanel is PanelStandard -> agentPanel.icons.entries.find { it.value == this@Icon }?.key
        else -> null
    }

    fun getProperIcon(element: IconElement): Icon {
        return if (element.subIconIndex < 0 || subIcons.isNullOrEmpty()) this
        else subIcons[element.subIconIndex]
    }

    fun getCyclicFrames(): Cyclic<Frame>? {
        return frames?.toCyclic(framesProperties?.frameMode ?: CycleMode.LOOP)
    }

}