package cc.trixey.invero.core.icon

import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.action.ScriptKether
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.animation.toCyclic
import cc.trixey.invero.core.item.Texture
import cc.trixey.invero.core.serialize.IconHandlerSerializer
import cc.trixey.invero.core.serialize.ListIconSerializer
import cc.trixey.invero.core.serialize.ListSlotSerializer
import cc.trixey.invero.core.serialize.ListStringSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames

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
    @JsonNames("key")
    override val id: String?,
    @JsonNames("require", "requirement", "rule", "if")
    val condition: ScriptKether?,
    @SerialName("update")
    val updatePeriod: Long = -1,
    @SerialName("relocate") @JsonNames("relocate", "refresh")
    val relocatePeriod: Long = -1,
    @JsonNames("frames-properties", "frames-prop")
    val framesProperties: Frame.Properties?,
    val frames: List<Frame>?,
    @Serializable @JsonNames("material", "mat")
    val texture: Texture?,
    val name: String?,
    @Serializable(with = ListStringSerializer::class)
    @JsonNames("lores")
    val lore: List<String>?,
    @Serializable @JsonNames("amt")
    val amount: Int?,
    @Serializable(with = ListSlotSerializer::class) @JsonNames("slots", "pos", "position", "positions")
    val slot: List<Slot>?,
    @Serializable(with = ListIconSerializer::class) @SerialName("sub")
    val subIcons: List<Icon>?,
    @SerialName("action")
    @JsonNames("actions", "handler", "click")
    @Serializable(with = IconHandlerSerializer::class)
    val handler: IconHandler?
) : AgentIcon() {

    @Transient
    var parent: AgentIcon? = null

    @Transient
    val defaultFrame = if (parent == null) Frame(null, texture, name, lore, amount, slot) else null

    init {
        subIcons?.forEach { it.parent = this }
        require(arrayOf(texture, frames).any { it != null }) {
            "Valid texture(material) for this icon is required"
        }
    }

    override fun invoke(session: Session, agent: AgentPanel, panel: Panel): IconElement {
        return IconElement(session, this, agent, panel).also { it.invoke() }
    }

    fun generateCyclicFrames(): Cyclic<Frame>? {
        return frames?.toCyclic(framesProperties?.frameMode ?: CycleMode.LOOP)
    }

}