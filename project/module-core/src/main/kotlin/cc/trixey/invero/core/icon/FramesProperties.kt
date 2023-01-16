package cc.trixey.invero.core.icon

import cc.trixey.invero.core.animation.CycleMode
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Invero
 * cc.trixey.invero.core.icon.FramesProperties
 *
 * @author Arasple
 * @since 2023/1/16 11:55
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
class FramesProperties(
    @SerialName("delay") @JsonNames("delay", "default-delay", "period")
    val defaultDelay: Long = 20,
    @SerialName("mode")
    val frameMode: CycleMode = CycleMode.LOOP
)