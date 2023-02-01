//package cc.trixey.invero.common.animation
//
//import cc.trixey.invero.common.animation.CycleMode
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.json.JsonNames
//
///**
// * Invero
// * cc.trixey.invero.common.animation.FrameProperties
// *
// * @author Arasple
// * @since 2023/1/31 15:52
// */
//@Serializable
//class FrameProperties(
//    @SerialName("delay")
//    @JsonNames("default-delay", "period")
//    val defaultDelay: Long = 20,
//    @SerialName("mode")
//    @JsonNames("type")
//    val frameMode: CycleMode = CycleMode.LOOP
//)