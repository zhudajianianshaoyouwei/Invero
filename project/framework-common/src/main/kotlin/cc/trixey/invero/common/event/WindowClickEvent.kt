package cc.trixey.invero.common.event

/**
 * Invero
 * cc.trixey.invero.common.event.WindowClickEvent
 *
 * @author Arasple
 * @since 2023/1/6 11:47
 */
interface WindowClickEvent : WindowEvent {

    val rawSlot: Int

    val clickType: ClickType

    var clickCancelled: Boolean

}