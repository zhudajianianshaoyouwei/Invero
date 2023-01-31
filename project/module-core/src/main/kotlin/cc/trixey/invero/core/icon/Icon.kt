package cc.trixey.invero.core.icon

import cc.trixey.invero.common.Panel
import cc.trixey.invero.core.AgentIcon
import cc.trixey.invero.core.AgentPanel
import cc.trixey.invero.core.Session
import cc.trixey.invero.core.action.ScriptKether
import cc.trixey.invero.core.animation.CycleMode
import cc.trixey.invero.core.animation.Cyclic
import cc.trixey.invero.core.animation.FrameProperties
import cc.trixey.invero.core.animation.toCyclic
import cc.trixey.invero.core.item.Frame
import cc.trixey.invero.core.serialize.IconHandlerSerializer
import cc.trixey.invero.core.serialize.ListIconSerializer
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
    override var id: String?,
    @JsonNames("if")
    val condition: ScriptKether?,
    val inherit: Boolean?,
    @SerialName("update")
    val periodUpdate: Long = -1,
    @SerialName("relocate")
    val periodRelocate: Long = -1,
    @SerialName("display")
    val defaultFrame: Frame,
    @JsonNames("frames-properties", "frames-prop", "prop")
    val framesProperties: FrameProperties?,
    val frames: List<Frame>?,
    @SerialName("sub")
    @Serializable(with = ListIconSerializer::class)
    val subIcons: List<Icon>?,
    @SerialName("action")
    @JsonNames("actions", "handler", "click")
    @Serializable(with = IconHandlerSerializer::class)
    val handler: IconHandler?
) : AgentIcon() {

    @Transient
    var parent: AgentIcon? = null

    init {
        require(arrayOf(defaultFrame.texture, frames).any { it != null }) {
            "Valid texture(material) for this icon is required"
        }
        condition?.default = false
    }

    override fun invoke(
        session: Session,
        agent: AgentPanel,
        panel: Panel,
        vars: Map<String, Any>
    ) = IconElement(session, this, agent, panel, vars).also {
        it.invoke()
    }

    fun generateCyclicFrames(): Cyclic<Frame>? {
        return frames?.toCyclic(framesProperties?.frameMode ?: CycleMode.LOOP)
    }

}